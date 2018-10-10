package ir.hotelairport.androidapp.airportHotels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.fabric.sdk.android.Fabric;
import ir.hotelairport.androidapp.HotelNewsFragment;
import ir.hotelairport.androidapp.LoginRegistrationFragment;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.TabsFragment;
import ir.hotelairport.androidapp.airportHotels.DailyStay.DailySearchFragment;
import ir.hotelairport.androidapp.airportHotels.DailyStay.DailyStayBookFragment;
import ir.hotelairport.androidapp.airportHotels.EventBus.DailyReserveResultBackEvent;
import ir.hotelairport.androidapp.airportHotels.EventBus.Ok;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReserveResultBackEvent;
import ir.hotelairport.androidapp.airportHotels.EventBus.ResultFragmentShow;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReturnButClick;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.Service.ServiceListFragment;
import ir.hotelairport.androidapp.airportHotels.ShortStay.SearchFragment;
import ir.hotelairport.androidapp.airportHotels.ShortStay.StayBookFragment;
import ir.hotelairport.androidapp.airportHotels.api.data.CheckBookStatusController;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.VoucherController;
import ir.hotelairport.androidapp.airportHotels.api.model.CheckStatusResponse;
import okhttp3.ResponseBody;

public class ComeFromWebActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int MY_PERMISION_REQUEST = 120;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView( R.layout.activity_main);

        checkStatus();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SearchFragment searchFragment = new SearchFragment();
        getFragmentManager().beginTransaction().replace(R.id.content_frame , searchFragment ).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    public void checkStatus(){
        JsonObject req = new JsonObject();
        req.addProperty("refId" , MyPreferenceManager.getInstace(getParent()).getRefId());
        CheckBookStatusController checkBookStatusController = new CheckBookStatusController(checkCallBack);
        checkBookStatusController.start(req , MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getAccess_token());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_daily) {
            DailySearchFragment dailySearchFragment = new DailySearchFragment();
            getFragmentManager().beginTransaction().replace( R.id.content_frame ,dailySearchFragment ).commit();
        } else if (id == R.id.nav_short_stay) {
            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace( R.id.content_frame ,searchFragment).commit();
        } else if (id == R.id.nav_services) {
            ServiceListFragment serviceListFragment = new ServiceListFragment();
            getFragmentManager().beginTransaction().replace( R.id.content_frame ,serviceListFragment).commit();
        }
        else if (id == R.id.about_hotel) {
            TabsFragment tabsFragment = TabsFragment.newInstance(2);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,tabsFragment).commit();
        }
        else if (id == R.id.hotel_news) {
            HotelNewsFragment hotelNewsFragment = new HotelNewsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,hotelNewsFragment).commit();
        }

        else if (id == R.id.guide) {
            TabsFragment tabsFragment =TabsFragment.newInstance(3);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,tabsFragment).commit();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    HotelApi.CheckBookStatusCallBack checkCallBack = new HotelApi.CheckBookStatusCallBack() {
        @Override
        public void onResponse(CheckStatusResponse response) {
            if (response.getErrorCode() == -1){
                MyPreferenceManager.getInstace(getApplicationContext()).putBookId(response.getBookId());
                DonePaymentFragment donePaymentFragment = new DonePaymentFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame , donePaymentFragment).commit();
            }
            else {
                CancelPaymentFragment cancelPaymentFragment = new CancelPaymentFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame , cancelPaymentFragment).commit();
            }
        }

        @Override
        public void onFailure(String cause) {

        }
    };


}
