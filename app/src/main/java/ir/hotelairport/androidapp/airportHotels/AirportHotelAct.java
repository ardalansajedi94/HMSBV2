package ir.hotelairport.androidapp.airportHotels;

import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import ir.hotelairport.androidapp.R;



public class AirportHotelAct extends AppCompatActivity {

    private FragmentManager fragManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airport_hotel);


        toolbar = findViewById(R.id.toolbar);

        this.initMenu();

        fragManager = getFragmentManager();
    }

    // ROOM BOOKING





    private void initMenu(){

        NavigationView navigationView = findViewById(R.id.navigation_view);

        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        {

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

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.hotel_primary));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {

                    case R.id.room_ws:

                        break;
                    case R.id.service_ws:

                        break;

                    default:

                }

                drawerLayout.closeDrawer(GravityCompat.START, true);
                return true;
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



}
