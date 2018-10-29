package net.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.hotelairport.androidapp.Models.CafeRestaurant;
import net.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import net.hotelairport.androidapp.Server.RequestInterface;
import net.hotelairport.androidapp.Server.ServerRequest;
import net.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableReserveFragment extends Fragment  implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{
    Button date_tv, time_tv;
    Spinner location_spinner;
    EditText count_et,explanation_et;
    Button send_req_btn;
    ProgressDialog progress;
    String selected_location;
    DatabaseHandler db;
    private ArrayList<ReserveLocations> locationses;
    ArrayList<CafeRestaurant> restaurants;
    ArrayList<CafeRestaurant> cafes;
    private SharedPreferences user_detail;
    private static final String TIMEPICKER = "TimePickerDialog", DATEPICKER = "DatePickerDialog";
    public TableReserveFragment() {
        // Required empty public constructor
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time =  hourOfDay + ":" + minute;
        time_tv.setText(time);
        send_req_btn.setEnabled(SubmitCondition());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date= year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
        date_tv.setText(date);
        send_req_btn.setEnabled(SubmitCondition());
    }
    @Override
    public void onResume() {
        super.onResume();
        send_req_btn.setEnabled(SubmitCondition());
    }
    private Boolean SubmitCondition()
    {
        return (!time_tv.getText().toString().equals(getResources().getString(R.string.time))&&!date_tv.getText().toString().equals(getResources().getString(R.string.select_date))&&!count_et.getText().toString().trim().equals(""));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_table_reserve, container, false);
        db=new DatabaseHandler(getActivity());
        send_req_btn = (Button) view.findViewById(R.id.send_req_btn);
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"table_reserve"));
        date_tv = (Button) view.findViewById(R.id.date_tv);
        time_tv = (Button) view.findViewById(R.id.time_tv);
        location_spinner = (Spinner) view.findViewById(R.id.location_spinner);
        count_et = (EditText) view.findViewById(R.id.count_et);
        explanation_et = (EditText) view.findViewById(R.id.note_et);

        TextWatcher submitConditionWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                send_req_btn.setEnabled(SubmitCondition());
            }
        };
        explanation_et.addTextChangedListener(submitConditionWatcher);
        count_et.addTextChangedListener(submitConditionWatcher);
        getCafeRestaurantsList();
        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_req_btn.setEnabled(SubmitCondition());
                if (user_detail.getInt(Constants.LANGUAGE_ID,0)==1)
                {

                    PersianCalendar now = new PersianCalendar();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            TableReserveFragment.this,
                            now.get(PersianCalendar.HOUR_OF_DAY),
                            now.get(PersianCalendar.MINUTE),
                            true
                    );
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d(TIMEPICKER, "Dialog was cancelled");
                        }
                    });
                    tpd.show(getActivity().getFragmentManager(), TIMEPICKER);
                }
                else
                {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    android.app.TimePickerDialog mTimePicker;
                    mTimePicker = new android.app.TimePickerDialog(getActivity(), new android.app.TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String time =  String.valueOf(selectedHour) + ":" + String.valueOf(selectedHour);
                            time_tv.setText(time);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.show();
                }
                send_req_btn.setEnabled(SubmitCondition());
            }
        });
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_req_btn.setEnabled(SubmitCondition());
                if (user_detail.getInt(Constants.LANGUAGE_ID,1)==1) {
                    PersianCalendar now = new PersianCalendar();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            TableReserveFragment.this,
                            now.getPersianYear(),
                            now.getPersianMonth(),
                            now.getPersianDay()
                    );
                    dpd.show(getActivity().getFragmentManager(), DATEPICKER);
                }
                else
                {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    android.app.DatePickerDialog dp = new  android.app.DatePickerDialog(getActivity(),new  android.app.DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date= year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
                            date_tv.setText(date);

                        }
                    },year,month,day);
                    dp.show();

                }
                send_req_btn.setEnabled(SubmitCondition());
            }
        });
        send_req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(time_tv.getText().toString(),date_tv.getText().toString(),selected_location,explanation_et.getText().toString(),Integer.parseInt(count_et.getText().toString()));
            }
        });
        return view;
    }
    private void getCafeRestaurantsList()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response ;
        response = requestInterface.getCafeRestaurantsList(user_detail.getString(Constants.JWT,""),user_detail.getInt(Constants.LANGUAGE_ID,1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            restaurants = resp.getRestaurants();
                           cafes = resp.getCafes();
                            locationses=new ArrayList<ReserveLocations>();
                            for (int i=0;i<restaurants.size();i++)
                            {
                                locationses.add(new ReserveLocations(restaurants.get(i).getId(),restaurants.get(i).getName(),1));
                            }
                            for (int i=0;i<cafes.size();i++)
                            {
                                locationses.add(new ReserveLocations(cafes.get(i).getId(),cafes.get(i).getName(),2));
                            }
                            List<String> locations_names = new ArrayList<String>();
                            for (int i=0;i<locationses.size();i++)
                            {
                                locations_names.add(locationses.get(i).getName());
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locations_names);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            location_spinner.setAdapter(dataAdapter);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });
    }
    private void sendRequest(String time,String date,String location,String note,int count)
    {
        if (note.trim().equals(""))
            note="\u00A0";
        note =note.replaceAll("[\n\r]", "");
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setTime(time);
        request.setDate(date);
        int selected_position=location_spinner.getSelectedItemPosition();
        if (selected_position<restaurants.size())
        {
            request.setPlace_type(1);
            request.setPlace_id(restaurants.get(selected_position).getId());
        }
        else
        {
            request.setPlace_type(2);
            request.setPlace_id(cafes.get(selected_position-restaurants.size()).getId());
        }
        request.setExplanation(note);
        request.setCount(count);
        Call<ServerResponse> response = requestInterface.send_reserve_table_request(user_detail.getString(Constants.JWT,""),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                Log.d("response",String.valueOf(response.code()));
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(), getResources().getString(R.string.not_allowed_user), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });

    }
    private class ReserveLocations
    {
        private int id,type;
        private String name;

        public ReserveLocations(int id,String name,int type)
        {
            this.id=id;
            this.type=type;
            this.name=name;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
