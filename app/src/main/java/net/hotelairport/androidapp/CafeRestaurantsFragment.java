package net.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import net.hotelairport.androidapp.Adapters.CafeRestaurantsListAdapter;
import net.hotelairport.androidapp.Models.CafeRestaurant;
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
public class CafeRestaurantsFragment extends Fragment {


    private int type; //4 : restaurant,5:cafes
    private ProgressDialog progress;
    private SharedPreferences user_detail;
    private ArrayList<CafeRestaurant>cafeRestaurants;
    private CafeRestaurantsListAdapter cafeRestaurantsListAdapter;
    private ListView cafeRestaurantsList;
    DatabaseHandler db;
    public CafeRestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cafe_restaurants, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        db=new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        cafeRestaurantsList=(ListView)view.findViewById(R.id.cafe_restaurants_list);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.type = bundle.getInt("type", 4);
        }
        switch (type) {
            case 4:
                activity.getSupportActionBar().setTitle(R.string.browse_restaurants);
                getRestaurantsList();
                break;
            case 5:
                activity.getSupportActionBar().setTitle(R.string.browse_cafes);
                getCafesList();
                break;
        }

        cafeRestaurants=new ArrayList<>();
        cafeRestaurantsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment=TabsFragment.newInstance(type,cafeRestaurants.get(position).getId());
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void getRestaurantsList() {
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
        response = requestInterface.get_restaurants(user_detail.getString(Constants.JWT,""),user_detail.getInt(Constants.LANGUAGE_ID, 1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("status code", String.valueOf(response.code()));
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            cafeRestaurants = resp.getRestaurants();
                            cafeRestaurantsListAdapter = new CafeRestaurantsListAdapter(cafeRestaurants, getActivity());
                            cafeRestaurantsList.setAdapter(cafeRestaurantsListAdapter);
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
            }
        });
    }

    private void getCafesList() {
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
        response = requestInterface.get_coffeshops(user_detail.getString(Constants.JWT,""),user_detail.getInt(Constants.LANGUAGE_ID, 1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("status code", String.valueOf(response.code()));
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            cafeRestaurants = resp.getRestaurants();
                            cafeRestaurantsListAdapter = new CafeRestaurantsListAdapter(cafeRestaurants, getActivity());
                            cafeRestaurantsList.setAdapter(cafeRestaurantsListAdapter);
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
            }
        });
    }


}
