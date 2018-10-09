package ir.hotelairport.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReserveResultBackEvent;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReturnButClick;
import ir.hotelairport.androidapp.airportHotels.ShortStay.SearchFragment;
import ir.hotelairport.androidapp.airportHotels.ShortStay.StayBookFragment;


public class GuestActivity extends AppCompatActivity {

    private static final String TAG = GuestActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DatabaseHandler(GuestActivity.this);
        setContentView(R.layout.activity_main);
        user_detail=getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initNavigationDrawer();
       SearchFragment fragment= new SearchFragment();
        getFragmentManager().beginTransaction().replace(R.id.content_frame , fragment).addToBackStack(null).commit();
    }

    private void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        MenuItem about_hotel_item=menu.findItem(R.id.about_hotel);
        MenuItem about_city_item=menu.findItem(R.id.about_city);
        MenuItem hotel_news_item=menu.findItem(R.id.hotel_news);
        MenuItem settings_item=menu.findItem(R.id.settings);
        about_hotel_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"about_hotel"));
        about_city_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"about_city"));
        hotel_news_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"hotel_news"));

        settings_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"settings"));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
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

                int id = menuItem.getItemId();

                    Fragment fragment;
                    FragmentTransaction fragmentTransaction;
                    switch (id) {
                        case R.id.about_hotel:
                            fragment =TabsFragment.newInstance(2);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,fragment).commit();
                            break;
                        case R.id.about_city:
                            fragment =TabsFragment.newInstance(1);
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,fragment).commit();
                            break;
                        case R.id.hotel_news:
                            fragment = new HotelNewsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,fragment).commit();
                            break;

                        case R.id.settings:
                            fragment = new GuestSettingsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,fragment).commit();
                            break;

                        case R.id.short_stay:
                            SearchFragment searchFragment = new SearchFragment();
                            getFragmentManager().beginTransaction().replace(R.id.content_frame ,searchFragment).commit();
                            break;
                        case R.id.services:

                            break;

                        default:
                            SearchFragment fragment1 = new SearchFragment();
                            getFragmentManager().beginTransaction().replace(R.id.content_frame ,fragment1).commit();
                    }



                drawerLayout.closeDrawer(Gravity.START,true);
                return true;
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
    @Subscribe
    public void onResuktBackEvent(ReserveResultBackEvent event){
        StayBookFragment stayBookFragment = new StayBookFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame , stayBookFragment).addToBackStack(null).commit();
    }
    @Subscribe
    public  void onReturnBut(ReturnButClick click){
        if (click.isClicked()){
            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame ,searchFragment).commit();
        }
        else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}


