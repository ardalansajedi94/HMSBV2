package ir.hotelairport.androidapp.airportHotels;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import ir.hotelairport.androidapp.Manifest;
import ir.hotelairport.androidapp.R;

import static io.fabric.sdk.android.Fabric.TAG;

public class DonePaymentFragment extends Fragment {
private static final int MY_PERMISION_REQUEST = 120;
    Button returnBut;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_done_payment ,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        returnBut= view.findViewById(R.id.return_but);
        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity() , MainActivity.class );
                getActivity().startActivity( intent );
                getActivity().finish();
            }
        });

    }
}
