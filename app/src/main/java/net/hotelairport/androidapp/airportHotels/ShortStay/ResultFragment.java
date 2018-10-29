package net.hotelairport.androidapp.airportHotels.ShortStay;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;


import java.util.ArrayList;

import net.hotelairport.androidapp.airportHotels.EventBus.Ok;
import net.hotelairport.androidapp.airportHotels.EventBus.ReserveResultBackEvent;
import net.hotelairport.androidapp.airportHotels.MainActivity;
import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.airportHotels.adapters.RoomListAdapter;
import net.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import net.hotelairport.androidapp.airportHotels.api.data.ReserveRoomController;
import net.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.api.model.ReserveRes;
import net.hotelairport.androidapp.airportHotels.api.model.Room;


public class ResultFragment extends Fragment {

    int[] counter ;
    Room[] confirm;
    int year ,month , day;
    AvailabilityRes response;
    View view;
    TextView name , date;
    RecyclerView recyclerView;
    Button send;
    String nameS;
    JsonArray roomList= new JsonArray();
    RelativeLayout main;
    ProgressBar progress;
    ArrayList<Room> roomConfirmation = new ArrayList<>(  );
    ArrayList<Integer> roomCount = new ArrayList<>(  );

    public ResultFragment() {

    }

    public ResultFragment newInstance(AvailabilityRes response , int year , int month , int day , String name) {

        Bundle args = new Bundle();
        this.year= year;
        this.month = month;
        this.day=day;
        this.nameS = name;
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);
        this.response = response;
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result , container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().post(new Ok(true));
        final RoomListAdapter roomListAdapter = new RoomListAdapter(response.getRooms() , getActivity() , getRooms);
        confirm = new Room[response.getRooms().size()];
        counter = new int[response.getRooms().size()];
        recyclerView = view.findViewById(R.id.hotel_list);
        main = view.findViewById(R.id.main);
        progress = view.findViewById(R.id.progress);
        send= view.findViewById(R.id.return_btn);
        name = view.findViewById(R.id.name);
        date = view.findViewById(R.id.date);
        name.setText(nameS);
        date.setText(PersianDigitConverter.PerisanNumber(String.valueOf(year)) + "/" + PersianDigitConverter.PerisanNumber(String.valueOf(month))+"/"+PersianDigitConverter.PerisanNumber(String.valueOf(day)));
        final LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(roomListAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0 ; i<counter.length ; i++){
                    if(confirm[i]!=null){
                        JsonObject object = new JsonObject();
                        object.addProperty( "roomId" , confirm[i].getRoom_id() );
                        object.addProperty( "count" , counter[i] );
                        roomList.add( object );
                        roomCount.add( counter[i] );
                        for (int j = 0 ; j<counter[i];j++)
                        roomConfirmation.add( confirm[i] );
                        roomCount.add( counter[i] );
                    }
                }

                if (roomList.size() != 0) {
                    progress.setVisibility(View.VISIBLE);
                    main.setVisibility(View.INVISIBLE);
                    JsonObject object = new JsonObject();
                    object.addProperty("refId", response.getRefId());
                    MyPreferenceManager.getInstace(getActivity()).putRefId(response.getRefId());
                    object.addProperty("api_token", MyPreferenceManager.getInstace(getActivity()).getToken());
                    object.add("rooms", roomList);
                    object.addProperty("ip", "0");
                    MyPreferenceManager.getInstace( getActivity() ).putRoom( roomConfirmation );
                    ReserveRoomController reserveRoomController = new ReserveRoomController(callBack);
                    reserveRoomController.start(object,MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
                }
                else {
                    Toast.makeText(getActivity() , "حداقل یک اتاق باید انتخاب شود", Toast.LENGTH_LONG).show();
                }

            }
        });
        this.view = view;
    }

    HotelApi.ReserveRoomCallBack callBack = new HotelApi.ReserveRoomCallBack() {
        @Override
        public void onResponse(ReserveRes res) {
                MyPreferenceManager.getInstace(getActivity()).putReservedId(res.getReservedId());
            ((MainActivity)getActivity()).onResuktBackEvent( new ReserveResultBackEvent(true) );

             }

        @Override
        public void onFailure(String cause) {

        }
    };

    RoomListAdapter.getRooms getRooms= new RoomListAdapter.getRooms() {
        @Override
        public void getRooms(int[] count, Room[] confirmRoom , int position) {
                counter[position] = count [position];
                confirm[position] = confirmRoom [position];

        }
    };

}
