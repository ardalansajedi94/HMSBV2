package ir.hotelairport.androidapp.airportHotels.Auth;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.ProfileController;
import ir.hotelairport.androidapp.airportHotels.api.model.ProfileResponse;

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
        ProfileController profileController = new ProfileController(callBack);
        profileController.start(MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
    }
    public void findViews(View view){
        name = view.findViewById(R.id.name);
        email= view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
    }
    HotelApi.ShowProfile callBack = new HotelApi.ShowProfile() {
        @Override
        public void onResponse(ProfileResponse response) {
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
