package ir.hotelairport.androidapp.airportHotels;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonObject;

import io.fabric.sdk.android.Fabric;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.CheckBookStatusController;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.model.CheckStatusResponse;

public class ComeFromWebActivity extends AppCompatActivity {
    private static final int MY_PERMISION_REQUEST = 120;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_progress);
        checkStatus();

    }


    @Override
    public void onBackPressed() {

    }

    public void checkStatus() {
        JsonObject req = new JsonObject();
        req.addProperty("refId", MyPreferenceManager.getInstace(getParent()).getRefId());
        CheckBookStatusController checkBookStatusController = new CheckBookStatusController(checkCallBack);
        checkBookStatusController.start(req, MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getToken_type() + " " + MyPreferenceManager.getInstace(getApplicationContext()).getLoginRes().getAccess_token());
    }


    HotelApi.CheckBookStatusCallBack checkCallBack = new HotelApi.CheckBookStatusCallBack() {
        @Override
        public void onResponse(CheckStatusResponse response) {
            if (response.getErrorCode() == -1) {
                MyPreferenceManager.getInstace(getApplicationContext()).putBookId(response.getBookId());
                DonePaymentFragment donePaymentFragment = new DonePaymentFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, donePaymentFragment).commit();
            } else {
                CancelPaymentFragment cancelPaymentFragment = new CancelPaymentFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, cancelPaymentFragment).commit();
            }
        }

        @Override
        public void onFailure(String cause) {

        }
    };


}
