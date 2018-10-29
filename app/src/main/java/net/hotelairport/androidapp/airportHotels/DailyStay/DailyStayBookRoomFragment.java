package net.hotelairport.androidapp.airportHotels.DailyStay;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.adapters.DailyPassengerStayBookAdapter;


public class DailyStayBookRoomFragment extends Fragment {
    int position;
    RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stay_book_room_daily ,container ,false );
    }

    public Fragment newInstance(int position  ){
        this.position = position;

        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DailyPassengerStayBookAdapter adapter = new DailyPassengerStayBookAdapter(getActivity() ,position);
        recyclerView= view.findViewById(R.id.pax_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }
}
