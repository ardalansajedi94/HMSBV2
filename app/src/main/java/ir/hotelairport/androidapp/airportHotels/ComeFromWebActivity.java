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

public class ComeFromWebActivity extends AppCompatActivity {
    private static final int MY_PERMISION_REQUEST = 120;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView( R.layout.activity_progress);
        checkStatus();

    }


    @Override
    public void onBackPressed() {

    }

    public void checkStatus(){
        JsonObject req = new JsonObject();
        req.addProperty("refId" , MyPreferenceManager.getInstace(getParent()).getRefId());
        CheckBookStatusController checkBookStatusController = new CheckBookStatusController(checkCallBack);
        checkBookStatusController.start(req , MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getAccess_token());
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
