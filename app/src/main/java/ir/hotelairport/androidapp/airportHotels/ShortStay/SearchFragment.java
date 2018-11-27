package ir.hotelairport.androidapp.airportHotels.ShortStay;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.Calendar;

import ir.hotelairport.androidapp.airportHotels.DailyStay.DailySearchFragment;

import ir.hotelairport.androidapp.airportHotels.EventBus.Ok;
import ir.hotelairport.androidapp.airportHotels.EventBus.ResultFragmentShow;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;

import ir.hotelairport.androidapp.airportHotels.PersianPicker;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.api.data.AvailabilityRoomController;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;
import ir.hotelairport.androidapp.airportHotels.Service.ServiceListFragment;
import saman.zamani.persiandate.PersianDate;


public class SearchFragment extends Fragment{

    private ConstraintLayout arrivalDate;
    private TextView dateText;
    private ConstraintLayout arrivalTime;
    private TextView timeText;
    private ProgressBar progressBar;
    private ConstraintLayout main;
    private ImageView serviceLogo ,dailyLogo;
    Button search;
    boolean flag=true;
    RadioGroup stayGroup;
    RadioButton radioButton;
    String name;

    String date =null , time =  null , Hour , min;
    int hour = 0;
    int mainHour = 0;
    int shYear , shMonth , shDay;
    private static final int MY_PERMISION_REQUEST = 120;
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
        return inflater.inflate(R.layout.fragment_short_stay , container ,false);
    }



    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        if (ContextCompat.checkSelfPermission(getActivity() , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date == null){
                    Toast.makeText(getActivity() , "ساعت ورود را انتخاب کنید" , Toast.LENGTH_LONG).show();
                }else if (time == null ){
                    Toast.makeText(getActivity() , "تاریخ ورود را انتخاب کنید" , Toast.LENGTH_LONG).show();
                }
                else {

                    int selectedId = stayGroup.getCheckedRadioButtonId();
                    radioButton =  view.findViewById(selectedId);
                    JsonObject object = new JsonObject();
                    object.addProperty("checkIn" , date + " " + time);
                    if (radioButton.getText().equals(getResources().getString( R.string.reserv_three_hour ))){
                        hour = mainHour;
                        hour+=3;

                        name = "اقامت کوتاه مدت سه ساعته";
                        object.addProperty("checkOut" ,  "3");
                    }else{
                        hour=mainHour;
                        hour+=6;

                        name = "اقامت کوتاه مدت شش ساعته";
                        object.addProperty("checkOut" ,  "6");
                    }
                    object.addProperty("api_token" , MyPreferenceManager.getInstace(getActivity()).getToken());
                    JsonObject obj = new JsonObject();
                    obj.addProperty("adults", "1");
                    obj.addProperty("childs", "0");
                    JsonArray rooms;
                    rooms=new JsonArray();
                    rooms.add(obj);
                    object.add("rooms" , rooms);
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
        dailyLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailySearchFragment dailySearchFragment = new DailySearchFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,dailySearchFragment ).commit();
            }
        } );
        final DatePickerDialog arrivalDatePicker = this.getArrivalDatePicker();
        final PersianPicker arrivalTimePicker = this.getArrivalTimePicker();
        arrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrivalDatePicker.show(getFragmentManager(), "dpd");
            }
        });

        arrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrivalTimePicker.show(getFragmentManager(), "tpd");
            }
        });


    }

    public void findViews(View view){
        arrivalDate = view.findViewById(R.id.date_btn);
        dateText = view.findViewById(R.id.date_text);
        timeText = view.findViewById(R.id.time_txt);
        arrivalTime = view.findViewById(R.id.time_btn);
        search = view.findViewById(R.id.search_btn);
        stayGroup = view.findViewById(R.id.time_group);
        serviceLogo = view.findViewById(R.id.service_reserve);
        dailyLogo = view.findViewById(R.id.daily_reserve);
        progressBar = view.findViewById( R.id.progress );
        main= view.findViewById( R.id.main );
    }



    public DatePickerDialog getArrivalDatePicker(){

        PersianCalendar now = new PersianCalendar();



        DatePickerDialog dpd =
                DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                shYear = year;
                                shMonth= monthOfYear+1;
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
                                dateText.setText(PersianDigitConverter.PerisanNumber(YearString+ "/"+MounthString+ "/"+ DayString));
                                date=jDate.getGrgYear() + "-" + jDate.getGrgMonth() + "-" + jDate.getGrgDay();
                            }
                        },
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay() + 1);


        dpd.setMinDate(now);

        return dpd;
    }

    public PersianPicker getArrivalTimePicker(){

        PersianCalendar now = new PersianCalendar();

        PersianPicker tpd = new PersianPicker();

        tpd.setStartTime(now.get( Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE));
        tpd.setOnTimeSetListener(new PersianPicker.OnTimeSetListener(){

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


                Hour = hourOfDay + "";
                min = minute + "";

                if(hourOfDay < 10){
                    DecimalFormat formatter = new DecimalFormat("##");
                    String HourString = formatter.format(Integer.valueOf(Hour));
                    String zero=formatter.format(00);
                    timeText.setText(PersianDigitConverter.PerisanNumber(zero+HourString+ ":" + zero+zero));
                    mainHour = hourOfDay;
                    time = hourOfDay+":00:00";
                }
                else {
                    DecimalFormat formatter = new DecimalFormat("##");
                    String HourString = formatter.format(Integer.valueOf(Hour));
                    String zero=formatter.format(00);
                    timeText.setText(PersianDigitConverter.PerisanNumber(HourString+ ":" + zero+zero));
                    mainHour = hourOfDay;
                    time = hourOfDay+":00:00";
                }

            }
        });



        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });


        tpd.setTitle("زمان خود را انتخاب کنید");

        return tpd;
    }
    HotelApi.AvailabilityRoomCallBack callBack = new HotelApi.AvailabilityRoomCallBack() {
        @Override
        public void onResponse(final AvailabilityRes res) {
            ResultFragment resultFragment = new ResultFragment();
            resultFragment.newInstance(res , shYear ,shMonth,shDay , name);
            getFragmentManager().beginTransaction().add(R.id.content_frame , resultFragment).addToBackStack(null).commit();
            EventBus.getDefault().post(new ResultFragmentShow(true)  );

        }

        @Override
        public void onFailure(String cause) {
            Toast.makeText(getActivity() , "از اتصال اینترنت خود اطمینان حاصل کنید" , Toast.LENGTH_LONG).show();
            progressBar.setVisibility( View.GONE );
            main.setVisibility( View.VISIBLE );
        }
    };


}
