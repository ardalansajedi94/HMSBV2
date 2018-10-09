package ir.hotelairport.androidapp;


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

import ir.hotelairport.androidapp.Adapters.AboutCityListAdapter;
import ir.hotelairport.androidapp.Models.BlogContent;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    ProgressDialog progress;
    private ListView helpsList;
    private ArrayList<BlogContent> blogContents;
    private AboutCityListAdapter aboutCityListAdapter;
    private SharedPreferences user_detail;
    int category;
    private DatabaseHandler db;
    SwipeRefreshLayout swipeRefreshLayout;
    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        db=new DatabaseHandler(getActivity());
        this.category=getArguments().getInt("category");
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        helpsList = (ListView) view.findViewById(R.id.helps_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        blogContents = new ArrayList<>();
        getHelpsList();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHelpsList();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.color_green, R.color.color_orange);
        helpsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new PostViewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", blogContents.get(i).getId());
                bundle.putInt("type", 4); // 1 for about hotel content and 2 for about city content and 3 for news and 4 for helps content
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
    public static HelpFragment newInstance(int category) {
        HelpFragment myFragment = new HelpFragment();

        Bundle args = new Bundle();
        args.putInt("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }
    private void getHelpsList() {
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
        Call<ServerResponse> response;
        response = requestInterface.get_helps( user_detail.getInt(Constants.LANGUAGE_ID, 1),category);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            blogContents = resp.getHelps();
                            aboutCityListAdapter = new AboutCityListAdapter(blogContents, getActivity());
                            helpsList.setAdapter(aboutCityListAdapter);

                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
