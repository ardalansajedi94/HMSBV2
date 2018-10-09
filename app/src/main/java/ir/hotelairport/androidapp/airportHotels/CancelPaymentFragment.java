package ir.hotelairport.androidapp.airportHotels;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.greenrobot.eventbus.EventBus;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReturnButClick;

public class CancelPaymentFragment extends Fragment {

    Button returnBut;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cancel_payment ,container, false);
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
