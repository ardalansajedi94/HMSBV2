package ir.hotelairport.androidapp.airportHotels.Auth;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.hotelairport.androidapp.R;

public class MyProfileFragment extends Fragment {

    TextView name , email , mobile;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile , container , false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);

    }
    public void findViews(View view){
        name = view.findViewById(R.id.name);
        email= view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
    }

}
