package ir.hotelairport.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.hotelairport.androidapp.airportHotels.Auth.SignInFragment;
import ir.hotelairport.androidapp.airportHotels.MainActivity;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;

public class AuthActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_auth);
        if (MyPreferenceManager.getInstace( this ).getLoginRes() == null) {
            SignInFragment signInFragment = new SignInFragment();
            getFragmentManager().beginTransaction().add( R.id.content_frame, signInFragment ).commit();
        }
        else {
            Intent myIntent = new Intent(this , MainActivity.class);
            this.startActivity(myIntent);
            this.finish( );
        }
    }
}
