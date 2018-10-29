package net.hotelairport.androidapp.airportHotels;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import net.hotelairport.androidapp.R;

public class WebTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);


        WebView browser = findViewById(R.id.webview);

        browser.canGoBack();
        browser.canGoForward();



        browser.loadUrl("https://aprodev.farazcrs.ir/");
    }
}
