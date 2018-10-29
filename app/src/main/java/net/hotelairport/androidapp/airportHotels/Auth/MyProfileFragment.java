package net.hotelairport.androidapp.airportHotels.Auth;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import net.hotelairport.androidapp.airportHotels.api.data.ProfileController;
import net.hotelairport.androidapp.airportHotels.api.model.ProfileResponse;

public class MyProfileFragment extends Fragment {

    TextView name , email , mobile;
    ConstraintLayout detail;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ProfileController profileController = new ProfileController(callBack);
        profileController.start(MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
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
        detail = view.findViewById(R.id.detail);
        progressBar = view.findViewById(R.id.progress);
    }
    HotelApi.ShowProfile callBack = new HotelApi.ShowProfile() {
        @Override
        public void onResponse(ProfileResponse response) {
            progressBar.setVisibility(View.GONE);
            detail.setVisibility(View.VISIBLE);
            name.setText(response.getFirstName() + " " + response.getLastName());
            email.setText(response.getEmail());
            mobile.setText(response.getMobile_number());
        }

        @Override
        public void onFailure(String cause) {

        }

        @Override
        public void onError(String cause) {

        }
    };

}
