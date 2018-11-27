package ir.hotelairport.androidapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.hotelairport.androidapp.Models.Profile;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedInActivity extends AppCompatActivity {
    private static final String TAG = GuestActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    TextView user_first_name_tv, user_room_no_tv;
    CircleImageView profile_iv;
    DisplayImageOptions options;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SharedPreferences user_detail;
    private ImageLoader imageLoader;
    private Boolean user_has_room = false;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(LoggedInActivity.this);
        imageLoader = ImageLoader.getInstance();
        setContentView(R.layout.activity_logged_in);
        user_detail = getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        if (user_detail.getInt(Constants.ROOM_NO, 0) != 0) {
            toolbar.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "services"));
            fragment = new ServicesFragment();
        } else {
            toolbar.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "about_hotel"));
            fragment = TabsFragment.newInstance(2);
        }
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
    }

    private void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        MenuItem services_item = menu.findItem(R.id.services);
        MenuItem basket_item = menu.findItem(R.id.basket);
        MenuItem time_line_item = menu.findItem(R.id.time_line);
        MenuItem suggestions_item = menu.findItem(R.id.opinion);
        MenuItem guide_item = menu.findItem(R.id.guide);
        MenuItem profile_item = menu.findItem(R.id.profile);
        MenuItem about_hotel_item = menu.findItem(R.id.about_hotel);
        MenuItem about_city_item = menu.findItem(R.id.about_city);
        MenuItem hotel_news_item = menu.findItem(R.id.hotel_news);
        MenuItem settings_item = menu.findItem(R.id.settings);
        about_hotel_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "about_hotel"));
        about_city_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "about_city"));
        hotel_news_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "hotel_news"));
        settings_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "settings"));
        services_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "services"));
        basket_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "basket"));
        time_line_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "time_line"));
        suggestions_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "Opinion"));
        guide_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "guide"));
        profile_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "profile"));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                drawerLayout.bringToFront();
                drawerLayout.requestLayout();
            }
        };
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (isInternetAvailable()) {
                    int id = menuItem.getItemId();


                    if ((id == R.id.services || id == R.id.basket || id == R.id.time_line) && (!user_has_room)) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(LoggedInActivity.this);
                        builder.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "user_not_staying"))
                                .setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "user_not_staying_desc"))
                                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    } else {
                        Fragment fragment;
                        FragmentTransaction fragmentTransaction;
                        switch (id) {
                            case R.id.services:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "services"));
                                fragment = new ServicesFragment();
                                break;
                            case R.id.basket:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "basket"));
                                fragment = new foodBasketFragment();
                                break;
                            case R.id.time_line:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "time_line"));
                                fragment = new TimeLineFragment();
                                break;
                            case R.id.about_hotel:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "about_hotel"));
                                fragment = TabsFragment.newInstance(2);
                                break;
                            case R.id.about_city:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "about_city"));
                                fragment = TabsFragment.newInstance(1);

                                break;
                            case R.id.hotel_news:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "hotel_news"));
                                fragment = new HotelNewsFragment();
                                break;
                            case R.id.opinion:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "Opinion"));
                                fragment = new SuggestionsFragment();
                                break;
                            case R.id.guide:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "guide"));
                                fragment = TabsFragment.newInstance(3);
                                break;
                            case R.id.settings:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "settings"));
                                fragment = new SettingsFragment();
                                break;
                            case R.id.profile:
                                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "profile"));
                                fragment = new ProfileFragment();
                                break;

                            default:
                                fragment = new ServicesFragment();
                        }
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment, "CURRENT_FRAGMENT");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(LoggedInActivity.this);
                    builder.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "need_internet"))
                                .setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "need_internet_desc"))
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                drawerLayout.closeDrawer(GravityCompat.START, true);
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
        user_first_name_tv = (TextView) header.findViewById(R.id.user_name);
        user_room_no_tv = (TextView) header.findViewById(R.id.user_room_no);
        profile_iv = (CircleImageView) header.findViewById(R.id.profile_image);

        user_detail = getSharedPreferences(Constants.USER_DETAIL, MODE_PRIVATE);
        user_first_name_tv.setText(user_detail.getString(Constants.USER_FIRST_NAME, "") + " " + user_detail.getString(Constants.USER_LAST_NAME, ""));

        if (user_detail.getInt(Constants.ROOM_NO, 0) != 0) {
            user_room_no_tv.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "room_no") + " : " + String.valueOf(user_detail.getInt(Constants.ROOM_NO, 0)));
        } else {
            user_room_no_tv.setText("");
        }
        imageLoader.displayImage(Constants.MEDIA_BASE_URL + user_detail.getString(Constants.PROFILE_IMAGE_NAME, ""), profile_iv, options);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentTransaction fragmentTransaction;
                getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "profile"));
                fragment = new ProfileFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START, true);
            }
        });
        getProfile();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void getProfile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response;
        if (user_detail.getInt(Constants.LANGUAGE_ID, 1) == 1)
            response = requestInterface.get_profile(user_detail.getString(Constants.JWT, ""), 1);
        else
            response = requestInterface.get_profile(user_detail.getString(Constants.JWT, ""), 2);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Profile profile = resp.getProfile();
                            File imageFile = imageLoader.getDiscCache().get(Constants.MEDIA_BASE_URL + profile.getProfile_image());
                            if (imageFile.exists()) {
                                imageFile.delete();
                            }
                            SharedPreferences.Editor editor = user_detail.edit();
                            editor.putString(Constants.PROFILE_IMAGE_NAME, resp.getProfile().getProfile_image());
                            editor.putInt(Constants.ROOM_NO, resp.getProfile().getRoom_no());
                            editor.apply();
                            DiskCacheUtils.removeFromCache(Constants.MEDIA_BASE_URL + profile.getProfile_image(), ImageLoader.getInstance().getDiskCache());
                            MemoryCacheUtils.removeFromCache(Constants.MEDIA_BASE_URL + profile.getProfile_image(), ImageLoader.getInstance().getMemoryCache());
//                            Picasso.with(LoggedInActivity.this).invalidate(Constants.MEDIA_BASE_URL + profile.getProfile_image());
                            user_first_name_tv.setText(profile.getFirstname() + " " + profile.getLastname());

                            if (profile.getRoom_no() != 0) {
                                user_room_no_tv.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "room_no") + " : " + String.valueOf(profile.getRoom_no()));
                                user_has_room = true;
                            } else {
                                user_room_no_tv.setText("");
                            }
                            // imageLoader.displayImage(Constants.MEDIA_BASE_URL+profile.getProfile_image(),profile_iv,options);
//                            Picasso mPicasso = Picasso.with(LoggedInActivity.this);
//                            mPicasso.load(Constants.MEDIA_BASE_URL + profile.getProfile_image()).placeholder(R.drawable.ic_person).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(profile_iv);
                        }
                        break;

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("error:", t.getMessage());
            }
        });
    }
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
