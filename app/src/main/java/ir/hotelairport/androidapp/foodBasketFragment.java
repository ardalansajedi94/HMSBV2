package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Adapters.CafeRestaurantsListAdapter;
import ir.hotelairport.androidapp.Adapters.FoodBasketListAdapter;
import ir.hotelairport.androidapp.Models.Basket;
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
public class foodBasketFragment extends Fragment {

    private ProgressDialog progress;
    private SharedPreferences user_detail;
    private ListView basket_list;
    private CafeRestaurantsListAdapter listAdapter;
    private ArrayList<Basket> basket;
    private Button submit_btn;
    private TextView basket_empty_tv;
    private FoodBasketListAdapter foodBasketListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseHandler db;
    public foodBasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_food_basket, container, false);
        db=new DatabaseHandler(getActivity());
        submit_btn=(Button)view.findViewById(R.id.submit_order_btn);
        basket_empty_tv=(TextView)view.findViewById(R.id.basket_empty_tv);
        basket_list=(ListView)view.findViewById(R.id.basket_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBasket(true);
            }
        });
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        getBasket(false);
        basket_empty_tv.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"basket_empty"));
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBasket();
            }
        });
        return view;
    }
    private  void submitBasket()
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
        Call<ServerResponse> response;
        response = requestInterface.submit_basket(user_detail.getString(Constants.JWT,""));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("status code", String.valueOf(response.code()));
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(),db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"submit_basket_success"), Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
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
    public void getBasket(final boolean is_refreshing)
    {
        if (!is_refreshing)
        {
            progress = new ProgressDialog(getActivity());
            progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response;
        response = requestInterface.get_basket(user_detail.getString(Constants.JWT,""));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                Log.d("status code", String.valueOf(response.code()));
                if (!is_refreshing)
                {
                    progress.dismiss();
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            basket=new ArrayList<Basket>();
                            basket.addAll(resp.getYour_basket());
                            if (basket.size()==0)
                            {
                                basket_empty_tv.setVisibility(View.VISIBLE);
                                basket_list.setVisibility(View.INVISIBLE);
                                submit_btn.setVisibility(View.GONE);
                            }
                            else
                            {
                                basket_empty_tv.setVisibility(View.INVISIBLE);
                                basket_list.setVisibility(View.VISIBLE);
                                submit_btn.setVisibility(View.VISIBLE);
                                foodBasketListAdapter=new FoodBasketListAdapter(basket,getActivity());
                                basket_list.setAdapter(foodBasketListAdapter);
                            }

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
                if (!is_refreshing)
                {
                    progress.dismiss();
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d("error:", t.getMessage());
            }
        });
    }

}
