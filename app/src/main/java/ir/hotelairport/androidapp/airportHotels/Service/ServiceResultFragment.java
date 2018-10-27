package ir.hotelairport.androidapp.airportHotels.Service;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.airportHotels.EventBus.Ok;
import ir.hotelairport.androidapp.airportHotels.EventBus.PassServicesToFragment;
import ir.hotelairport.androidapp.airportHotels.EventBus.PassServicesToPax;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReserveButtonClickedServices;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReserveResultBackEvent;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.adapters.ServiceListAdapter;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.model.ReserveRes;
import ir.hotelairport.androidapp.airportHotels.api.model.Service;
import ir.hotelairport.androidapp.airportHotels.api.model.ServicesResponse;



public class ServiceResultFragment extends Fragment {

    ServicesResponse res;
    TextView name , date;
    RecyclerView recyclerView;
    Button send;
    JsonArray servicesArray = new JsonArray();
    String nameS;
    int year ,month , day;
    @Subscribe
    public void onServiceBack(final PassServicesToFragment services){

        if (services.getServiceList().size() != 0) {
            nextPage();
            getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    EventBus.getDefault().post(new PassServicesToPax(servicesArray, services.getServiceList() , services.getCount()));
                }
            });
        }
        else {
            Toast.makeText(getActivity() , "شما هیچ سرویسی در لیست خرید ندارید",Toast.LENGTH_LONG).show();
        }





    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register( this );
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister( this );
    }

    public ServiceResultFragment() {

    }

    public ServiceResultFragment newInstance(ServicesResponse response , int year , int month , int day , String name) {

        Bundle args = new Bundle();
        this.year= year;
        this.month = month;
        this.day=day;
        this.nameS = name;
        ServiceResultFragment fragment = new ServiceResultFragment();
        fragment.setArguments(args);
        this.res = response;
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
        recyclerView=view.findViewById( R.id.hotel_list);
        send=view.findViewById( R.id.return_btn);
        name = view.findViewById(R.id.name);
        date = view.findViewById(R.id.date);
        name.setText(nameS);
        date.setText(PersianDigitConverter.PerisanNumber(String.valueOf(year)) + "/" + PersianDigitConverter.PerisanNumber(String.valueOf(month))+"/"+PersianDigitConverter.PerisanNumber(String.valueOf(day)));
        EventBus.getDefault().post(new Ok(true));
        MyPreferenceManager.getInstace(getActivity()).putRefId(res.getRef_id());
        List<Service> actServices = new ArrayList<>();
        for (int i = 0 ; i<res.getServices().size() ; i++){
                if (res.getServices().get(i).getHotel_id()==1){
                    actServices.add(res.getServices().get(i));
                }
                else {
                    if (res.getServices().get(i).getTitle_fa().equals("صبحانه") ){
                        actServices.add(res.getServices().get(i));
                    }

            }
        }
             if (actServices.size() == 0){

             }
             else {


              ServiceListAdapter serviceListAdapter = new ServiceListAdapter(getActivity() , actServices.size() , actServices);
              recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
              recyclerView.setAdapter(serviceListAdapter);
          }
          send.setOnClickListener( new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  EventBus.getDefault().post(new ReserveButtonClickedServices(true));
              }
          } );

    }

    HotelApi.ReserveRoomCallBack callBack = new HotelApi.ReserveRoomCallBack() {
        @Override
        public void onResponse(ReserveRes res) {
                MyPreferenceManager.getInstace(getActivity()).putReservedId(res.getReservedId());
                EventBus.getDefault().post(new ReserveResultBackEvent(true));

             }

        @Override
        public void onFailure(String cause) {

        }
    };



    public void nextPage() {

        ServicePaxFragment servicePaxFragment = new ServicePaxFragment();
        getFragmentManager().beginTransaction().add(R.id.content_frame , servicePaxFragment).addToBackStack(null).commit();
    }

}
