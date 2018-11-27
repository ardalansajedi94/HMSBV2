package ir.hotelairport.androidapp.airportHotels;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import co.ronash.pushe.Pushe;
import io.fabric.sdk.android.Fabric;
import ir.hotelairport.androidapp.AuthActivity;
import ir.hotelairport.androidapp.HotelNewsFragment;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.TabsFragment;
import ir.hotelairport.androidapp.airportHotels.Auth.MyProfileFragment;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Pushe.initialize(this, true);
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
        if (getIntent().getIntExtra("fragmentNumber", 0) == 1000) {
            MyTicketFragment myTicketFragment = new MyTicketFragment();
            getFragmentManager().beginTransaction().add(R.id.content_frame, myTicketFragment).addToBackStack(null).commit();
        } else {
            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, searchFragment).commit();
        }

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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_daily) {
            DailySearchFragment dailySearchFragment = new DailySearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, dailySearchFragment).commit();
        } else if (id == R.id.nav_short_stay) {
            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, searchFragment).commit();
        } else if (id == R.id.nav_services) {
            ServiceListFragment serviceListFragment = new ServiceListFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, serviceListFragment).commit();
        } else if (id == R.id.about_hotel) {
            TabsFragment tabsFragment = TabsFragment.newInstance(2);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, tabsFragment).commit();
        } else if (id == R.id.hotel_news) {
            HotelNewsFragment hotelNewsFragment = new HotelNewsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, hotelNewsFragment).commit();
        } else if (id == R.id.purch_list) {
            MyTicketFragment myTicketFragment = new MyTicketFragment();
            getFragmentManager().beginTransaction().add(R.id.content_frame, myTicketFragment).addToBackStack(null).commit();
        } else if (id == R.id.profile) {
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, myProfileFragment).commit();
        } else if (id == R.id.guide) {
            TabsFragment tabsFragment = TabsFragment.newInstance(3);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, tabsFragment).commit();
        } else if (id == R.id.exit) {
            MyPreferenceManager.getInstace(this).putLoginRes(null);
            Intent myIntent = new Intent(this, AuthActivity.class);
            this.startActivity(myIntent);
            this.finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onResuktBackEvent(ReserveResultBackEvent event) {
        StayBookFragment stayBookFragment = new StayBookFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, stayBookFragment).commit();
    }

    public void onDailyResuktBackEvent(DailyReserveResultBackEvent event) {
        DailyStayBookFragment dailyStayBookFragment = new DailyStayBookFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, dailyStayBookFragment).commit();
    }

    public void RulesFragmentEvent() {
        RulesFragment rulesFragment = new RulesFragment();
        getFragmentManager().beginTransaction().add(R.id.content_frame, rulesFragment).addToBackStack(null).commit();
    }

    @Subscribe
    public void onReturnBut(ReturnButClick click) {
        if (click.isClicked()) {
            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, searchFragment).commit();
        } else {
            ServiceListFragment serviceListFragment = new ServiceListFragment();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, serviceListFragment).commit();
        }
    }

    @Subscribe
    private void resultFragmentShowed(ResultFragmentShow show) {
        EventBus.getDefault().post(new Ok(true));
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

}
