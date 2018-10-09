package ir.hotelairport.androidapp.airportHotels;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.hotelairport.androidapp.HotelNewsFragment;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.TabsFragment;
import ir.hotelairport.androidapp.airportHotels.DailyStay.DailySearchFragment;
import ir.hotelairport.androidapp.airportHotels.Service.ServiceListFragment;
import ir.hotelairport.androidapp.airportHotels.ShortStay.SearchFragment;

public class FirstFragment extends Fragment {
    ConstraintLayout daily , shortStay, service, hotel,city,news;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_first , container , false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        daily = view.findViewById( R.id.daily );
        shortStay = view.findViewById( R.id.short_stay );
        service = view.findViewById( R.id.service );
        hotel = view.findViewById( R.id.about_hotel );
        city = view.findViewById( R.id.about_city );
        news = view.findViewById( R.id.hotel_news );


        daily.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailySearchFragment dailySearchFragment = new DailySearchFragment();
                getFragmentManager().beginTransaction().add( R.id.content_frame ,dailySearchFragment ).addToBackStack( null ).commit();
            }
        } );

        shortStay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                getFragmentManager().beginTransaction().add( R.id.content_frame ,searchFragment).addToBackStack( null ).commit();
            }
        } );
        service.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceListFragment serviceListFragment = new ServiceListFragment();
                getFragmentManager().beginTransaction().add( R.id.content_frame ,serviceListFragment).addToBackStack( null ).commit();
            }
        } );


       hotel.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               TabsFragment tabsFragment = TabsFragment.newInstance(2);
               ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,tabsFragment).addToBackStack( null ).commit();
           }
       } );

        city.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabsFragment tabsFragment =TabsFragment.newInstance(3);
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,tabsFragment).addToBackStack( null ).commit();
            }
        } );

        news.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotelNewsFragment hotelNewsFragment = new HotelNewsFragment();
                ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,hotelNewsFragment).addToBackStack( null ).commit();
            }
        } );



    }


}
