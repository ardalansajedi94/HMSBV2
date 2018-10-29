package net.hotelairport.androidapp.airportHotels;

import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class clickableText extends ClickableSpan {


    @Override
    public void onClick(View widget) {

        Log.d("tag", "will it work?");

        Toast.makeText(widget.getContext(), "worked?", Toast.LENGTH_LONG).show();

    }
}
