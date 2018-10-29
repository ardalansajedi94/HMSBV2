package net.hotelairport.androidapp.airportHotels.Service;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import net.hotelairport.androidapp.airportHotels.DailyStay.DailySearchFragment;
import net.hotelairport.androidapp.airportHotels.PersianDate;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.EventBus.Ok;
import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.airportHotels.ShortStay.SearchFragment;
import net.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import net.hotelairport.androidapp.airportHotels.api.data.ServiceListController;
import net.hotelairport.androidapp.airportHotels.api.model.ServicesResponse;


public class ServiceListFragment extends Fragment {



    private ConstraintLayout arrivalTime , arrivalDate;
    TextView timeText ,dateText;
    ImageView shortStayLogo , dailyLogo;
    private Button accept;
    private String hour=null;
    private String min=null;
    private String checkIn=null;
    int shYear , shMonth,shDay;
    boolean flag = false;
    private ProgressBar progressBar;
    private ConstraintLayout main;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_service_list, container, false);

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrivalTime = view.findViewById(R.id.time_btn);
        arrivalDate = view.findViewById(R.id.date_btn);
        accept= view.findViewById(R.id.search_btn);
        dateText = view.findViewById(R.id.date_text);
        timeText = view.findViewById(R.id.time_txt);
        shortStayLogo = view.findViewById(R.id.short_reserve);
        dailyLogo = view.findViewById(R.id.daily_reserve);
        progressBar = view.findViewById( R.id.progress );
        main= view.findViewById( R.id.main );



        final DatePickerDialog dpd = this.getArrivalDatePicker();

        final TimePickerDialog tpd = this.getArrivalTimePicker();

        arrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(), "dpd");
                flag=true;
            }
        });

        arrivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    tpd.show(getFragmentManager(), "dpd");
                    flag = false;
                }
                else {
                    Toast.makeText(getActivity() , "ابتدا تاریخ خود را انتخاب کنید", Toast.LENGTH_LONG).show();

                }
            }
        });

        dailyLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DailySearchFragment dailySearchFragment = new DailySearchFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,dailySearchFragment ).commit();
            }
        } );
        shortStayLogo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                getFragmentManager().beginTransaction().replace( R.id.content_frame ,searchFragment ).commit();
            }
        } );
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ServiceListController serviceListController = new ServiceListController(callBack);
                JsonObject object = new JsonObject();
                object.addProperty("api_token" , MyPreferenceManager.getInstace(getActivity()).getToken());
                serviceListController.start(object,MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
                main.setVisibility( View.GONE );
                progressBar.setVisibility( View.VISIBLE );

            }
        });


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

                                DecimalFormat formatter = new DecimalFormat("##");
                                String YearString = formatter.format(Integer.valueOf(year));
                                String MounthString = formatter.format(Integer.valueOf(m));
                                String DayString = formatter.format(Integer.valueOf(dayOfMonth));
                                dateText.setText(PersianDigitConverter.PerisanNumber(YearString+ "/"+MounthString+ "/"+ DayString));

                                checkIn = String.valueOf(jDate.getGrgYear())+"-"+String.valueOf(jDate.getGrgMonth()) +"-"+ String.valueOf(jDate.getGrgDay());
                            }
                        },
                        now.getPersianYear(),
                        now.getPersianMonth(),
                        now.getPersianDay() + 1);

        dpd.setMinDate(now);

        return dpd;
    }

    public TimePickerDialog getArrivalTimePicker(){

        PersianCalendar now = new PersianCalendar();

        TimePickerDialog tpd = new TimePickerDialog();

        tpd.setStartTime(now.get(PersianCalendar.HOUR_OF_DAY),
                now.get(PersianCalendar.MINUTE));

        tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


                hour = hourOfDay + "";
                min = minute + "";

                if(hourOfDay < 10){
                    DecimalFormat formatter = new DecimalFormat("##");
                    String HourString = formatter.format(Integer.valueOf(hour));
                    String zero=formatter.format(00);
                    timeText.setText(PersianDigitConverter.PerisanNumber(zero+HourString+ ":" + zero+zero));

                }
                else {
                    DecimalFormat formatter = new DecimalFormat("##");
                    String HourString = formatter.format(Integer.valueOf(hour));
                    String zero=formatter.format(00);
                    timeText.setText(PersianDigitConverter.PerisanNumber(HourString+ ":" + zero+zero));

                }
                checkIn += (" " + hour+":00:00");
                MyPreferenceManager.getInstace(getActivity()).putCheckIn(checkIn);

                String time = hour + min;
                int inTime = Integer.parseInt(time);



            }
        });



        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });


        tpd.setTitle("time");

        return tpd;
    }


    HotelApi.ServiceListCallBack callBack = new HotelApi.ServiceListCallBack() {
        @Override
        public void onResponse(ServicesResponse res) {
            MyPreferenceManager.getInstace(getActivity()).putRefId(res.getRef_id());
            ServiceResultFragment serviceResultFragment = new ServiceResultFragment();
            serviceResultFragment.newInstance( res ,shYear,shMonth,shDay,"سرویس ها" );
            getFragmentManager().beginTransaction().add( R.id.content_frame , serviceResultFragment ).addToBackStack( null ).commit();
        }

        @Override
        public void onFailure(String cause) {
            Toast.makeText(getActivity() , "از اتصال اینترنت خود اطمینان حاصل کنید" , Toast.LENGTH_LONG).show();
            progressBar.setVisibility( View.GONE );
            main.setVisibility( View.VISIBLE );
        }
    };
}
