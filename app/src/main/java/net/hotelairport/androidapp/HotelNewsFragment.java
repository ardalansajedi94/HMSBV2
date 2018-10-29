package net.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import net.hotelairport.androidapp.Adapters.AboutCityListAdapter;
import net.hotelairport.androidapp.Models.BlogContent;
import net.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import net.hotelairport.androidapp.Server.RequestInterface;
import net.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotelNewsFragment extends Fragment {

    private ListView aboutCityList;
    private ArrayList<BlogContent> blogContents;
    private AboutCityListAdapter aboutCityListAdapter;
    private SharedPreferences user_detail;
    ProgressDialog progress;
    private DatabaseHandler db;
    SwipeRefreshLayout swipeRefreshLayout;
    public HotelNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_hotel_news, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        aboutCityList = (ListView) view.findViewById(R.id.hotel_news_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        blogContents=new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsContent();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.color_green, R.color.color_orange);
        getNewsContent();
        aboutCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new PostViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", blogContents.get(i).getId());
                bundle.putInt("type", 3); // 1 for about hotel content and 2 for about city content and 3 for news
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
    private void getNewsContent()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response ;
        response = requestInterface.hotel_news(user_detail.getInt(Constants.LANGUAGE_ID,1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("status code",String.valueOf(response.code()));
                progress.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            blogContents=resp.getNews();
                            aboutCityListAdapter = new AboutCityListAdapter(blogContents,getActivity());
                            aboutCityList.setAdapter(aboutCityListAdapter);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    }
