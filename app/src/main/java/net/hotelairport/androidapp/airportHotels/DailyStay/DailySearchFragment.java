package net.hotelairport.androidapp.airportHotels.DailyStay;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import net.hotelairport.androidapp.airportHotels.PersianDate;
;
import net.hotelairport.androidapp.airportHotels.EventBus.Ok;
import net.hotelairport.androidapp.airportHotels.EventBus.ResultFragmentShow;
import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;

import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.Service.ServiceListFragment;
import net.hotelairport.androidapp.airportHotels.ShortStay.SearchFragment;
import net.hotelairport.androidapp.airportHotels.api.data.AvailabilityRoomController;
import net.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import net.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;

public class DailySearchFragment extends Fragment {
    private ConstraintLayout arrivalDate;
    private TextView dateText;
    private ConstraintLayout arrivalCheckOut;
    private TextView checkOutDate;
    ImageView shortStayLogo ,serviceLogo;
    Button search;
    int shYear , shMonth,shDay;
    private ProgressBar progressBar;
    private ConstraintLayout main;
    boolean flag=false, dateFlag=false;
    RadioGroup stayGroup;
    RadioButton radioButton;


    String date =null , cDate =  null  ;
    int Iday = 0 , Imounth= 0 , Iyear= 0;


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Subscribe
    public void onNextPageShown(Ok ok){
        progressBar.setVisibility( View.GONE );
        main.setVisibility( View.VISIBLE );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_search , container ,false);
    }



    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        shortStayLogo= view.findViewById( R.id.short_reserve );
        serviceLogo= view.findViewById( R.id.service_reserve );
        progressBar = view.findViewById( R.id.progress );
        main= view.findViewById( R.id.main );


        shortStayLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,searchFragment ).commit();
            }
        } );
        serviceLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceListFragment serviceListFragment = new ServiceListFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,serviceListFragment ).commit();
            }
        } );
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date == null){
                    Toast.makeText(getActivity() , "تاریخ ورود را انتخاب کنید" , Toast.LENGTH_LONG).show();
                }else if (cDate == null ){
                    Toast.makeText(getActivity() , "تاریخ خروج را انتخاب کنید" , Toast.LENGTH_LONG).show();
                }
                else {

                    JsonObject object = new JsonObject();
                    object.addProperty("checkIn" , date);
                    object.addProperty("checkOut" , cDate);
                    object.addProperty("api_token" , MyPreferenceManager.getInstace(getActivity()).getToken());
                    JsonObject obj = new JsonObject();
                    obj.addProperty("adults", "1");
                    obj.addProperty("childs", "0");
                    JsonArray rooms;
                    rooms=new JsonArray();
                    rooms.add(obj);
                    object.add("rooms" , rooms);
                    dateFlag=false;
                    AvailabilityRoomController availabilityRoomController = new AvailabilityRoomController(callBack);
                    availabilityRoomController.start(object,MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
                    main.setVisibility( View.GONE );
                    progressBar.setVisibility( View.VISIBLE );
                }
            }
        });
        serviceLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceListFragment serviceListFragment = new ServiceListFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,serviceListFragment ).commit();
            }
        } );


        arrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog arrivalDatePicker = getArrivalDatePicker();
                arrivalDatePicker.show(getFragmentManager(), "dpd");


            }
        });
        arrivalCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateFlag) {
                    final DatePickerDialog arrivalDatePickerCheckOut = getArrivalDatePickerCheckOut();
                    arrivalDatePickerCheckOut.show( getFragmentManager(), "dpd" );
                }
                else
                    Toast.makeText(getActivity() , "ابتدا تاریخ ورود را انتخاب کنید" , Toast.LENGTH_LONG).show();
            }
        });



    }

    public void findViews(View view){
        arrivalDate = view.findViewById(R.id.date_btn);
        dateText = view.findViewById(R.id.date_text);
        checkOutDate= view.findViewById(R.id.time_txt);
        arrivalCheckOut = view.findViewById(R.id.time_btn);
        search = view.findViewById(R.id.search_btn);
        stayGroup = view.findViewById(R.id.time_group);



    }



    public DatePickerDialog getArrivalDatePicker(){

        PersianCalendar now = new PersianCalendar();



        DatePickerDialog dpd =
                DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                 shYear=year;
                                 shMonth=monthOfYear+1;
                                 shDay=dayOfMonth;
                                PersianDate jDate = new PersianDate();
                                jDate.setShYear(year);
                                jDate.setShMonth(monthOfYear + 1);
                                jDate.setShDay(dayOfMonth);
                                int m = monthOfYear + 1;
                                DecimalFormat formatter = new DecimalFormat("#");
                                String YearString = formatter.format(Integer.valueOf(year));
                                String MounthString = formatter.format(Integer.valueOf(m));
                                String DayString = formatter.format(Integer.valueOf(dayOfMonth));
                                dateText.setText( PersianDigitConverter.PerisanNumber(YearString+ "/"+MounthString+ "/"+ DayString));
                                date=jDate.getGrgYear() + "-" + jDate.getGrgMonth() + "-" + jDate.getGrgDay();
                                Iday=dayOfMonth;
                                Imounth = monthOfYear;
                                Iyear=year;
                                flag=true;
                                dateFlag=true;
                            }
                        },
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay() + 1);


        dpd.setMinDate(now);

        return dpd;
    }

    public DatePickerDialog getArrivalDatePickerCheckOut(){

        PersianCalendar now = new PersianCalendar();

        if (flag) {
            flag=false;
            Iday++;
            if (Imounth < 7 && Iday == 32) {
                Iday = 1;
                Imounth++;
            } else if (Imounth > 6 && Iday == 31) {
                Iday = 1;
                Imounth++;
                if (Imounth == 13) {
                    Iyear++;
                    Imounth = 1;
                }
            }
        }
        DatePickerDialog dpd =
                DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


                                PersianDate jDate = new PersianDate();
                                jDate.setShYear(year);
                                jDate.setShMonth(monthOfYear + 1);
                                jDate.setShDay(dayOfMonth);

                                int m = monthOfYear + 1;

                                DecimalFormat formatter = new DecimalFormat("#");
                                String YearString = formatter.format(Integer.valueOf(year));
                                String MounthString = formatter.format(Integer.valueOf(m));
                                String DayString = formatter.format(Integer.valueOf(dayOfMonth));
                                checkOutDate.setText( PersianDigitConverter.PerisanNumber(YearString+ "/"+MounthString+ "/"+ DayString));
                                cDate=jDate.getGrgYear() + "-" + jDate.getGrgMonth() + "-" + jDate.getGrgDay();
                            }
                        },
                        Iyear,
                        Imounth,
                        Iday);

        now.setPersianDate( Iyear,Imounth,Iday );

        dpd.setMinDate(now);

        return dpd;
    }


    HotelApi.AvailabilityRoomCallBack callBack = new HotelApi.AvailabilityRoomCallBack() {
        @Override
        public void onResponse(final AvailabilityRes res) {
            DailyResultFragment dailyResultFragment = new DailyResultFragment();
            dailyResultFragment.newInstance(res , shYear ,shMonth,shDay , "اقامت روزانه");
            getFragmentManager().beginTransaction().add(R.id.content_frame , dailyResultFragment).addToBackStack(null).commit();
            EventBus.getDefault().post(new ResultFragmentShow(true));

        }

        @Override
        public void onFailure(String cause) {
            Toast.makeText(getActivity() , "از اتصال اینترنت خود اطمینان حاصل کنید" , Toast.LENGTH_LONG).show();
            progressBar.setVisibility( View.GONE );
            main.setVisibility( View.VISIBLE );
        }
    };


}
