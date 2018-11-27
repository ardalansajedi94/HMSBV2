package ir.hotelairport.androidapp;


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
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Adapters.CafeMenuListAdapter;
import ir.hotelairport.androidapp.Adapters.RestaurantMenuListAdapter;
import ir.hotelairport.androidapp.Models.CafeMenuItem;
import ir.hotelairport.androidapp.Models.RestaurantMenuItem;
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
public class MenuFragment extends Fragment {


    private GridView menuGrid;
    private int type; //4 : restaurant,5:cafes
    private int id, category;
    private ProgressDialog progress;
    private SharedPreferences user_detail;
    private ArrayList<RestaurantMenuItem> restaurantMenuItems;
    private ArrayList<CafeMenuItem> cafeMenuItems;
    private CafeMenuListAdapter cafeMenuListAdapter;
    private RestaurantMenuListAdapter restaurantMenuListAdapter;
    DatabaseHandler db;

    public MenuFragment() {
        // Required empty public constructor
    }


    public static MenuFragment newInstance(int type, int id, int category) {
        MenuFragment myFragment = new MenuFragment();

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        args.putInt("category", category);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        db = new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        menuGrid = (GridView) view.findViewById(R.id.menu_grid);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.type = bundle.getInt("type", 4);
            this.id = bundle.getInt("id", 1);
            this.category = bundle.getInt("category", 1);
            getMenu();
        }
        menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new MenuItemFragment();
                Bundle bundle = new Bundle();
                if (type == 4) {
                    bundle.putInt("id", restaurantMenuItems.get(position).getId());

                } else {
                    bundle.putInt("id", cafeMenuItems.get(position).getId());

                }
                bundle.putInt("type", type - 3); //
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.menu);
        return view;
    }

    private void getMenu() {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
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
        if (type == 4) {
            response = requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT, ""), "restaurants/" + String.valueOf(id) + "/menu?cat_id=" + String.valueOf(category));
        } else {
            response = requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT, ""), "coffeeShops/" + String.valueOf(id) + "/menu?cat_id=" + String.valueOf(category));
        }
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {


                            if (type == 4) {
                                restaurantMenuItems = resp.getRestaurant_menu();
                                restaurantMenuListAdapter = new RestaurantMenuListAdapter(restaurantMenuItems, getActivity());
                                menuGrid.setAdapter(restaurantMenuListAdapter);
                            } else {
                                cafeMenuItems = resp.getCafe_menu();
                                cafeMenuListAdapter = new CafeMenuListAdapter(cafeMenuItems, getActivity());
                                menuGrid.setAdapter(cafeMenuListAdapter);
                            }

                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
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
