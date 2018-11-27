package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;

import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerRequest;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestWakeupServiceFragment extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    Button date_tv, time_tv;
    EditText note_et;
    Button send_req_btn;
    ProgressDialog progress;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    private static final String TIMEPICKER = "TimePickerDialog", DATEPICKER = "DatePickerDialog";

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
    public RequestWakeupServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_request_wakeup, container, false);
        db=new DatabaseHandler(getActivity());
        send_req_btn = (Button) view.findViewById(R.id.send_req_btn);
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"wake_up"));
        date_tv = (Button) view.findViewById(R.id.date_tv);
        time_tv = (Button) view.findViewById(R.id.time_tv);
        note_et = (EditText) view.findViewById(R.id.note_et);
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_req_btn.setEnabled(SubmitCondition());
                if (user_detail.getInt(Constants.LANGUAGE_ID,0)==1)
                {

                    PersianCalendar now = new PersianCalendar();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            RequestWakeupServiceFragment.this,
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
                            send_req_btn.setEnabled(SubmitCondition());
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
                            RequestWakeupServiceFragment.this,
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
                            send_req_btn.setEnabled(SubmitCondition());

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
                sendRequest(time_tv.getText().toString(),date_tv.getText().toString(),note_et.getText().toString());
            }
        });
        return view;
    }
    private void sendRequest(String time,String date,String note)
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
        request.setExplanation(note);
        request.setCount(1);
        Call<ServerResponse> response = requestInterface.send_wake_up_request(user_detail.getString(Constants.JWT,""),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                Log.d("response",String.valueOf(response.code()));
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"send_success"), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(),db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"not_allowed_user"), Toast.LENGTH_SHORT).show();
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
    private Boolean SubmitCondition()
    {
        return (!time_tv.getText().toString().equals(getResources().getString(R.string.time))&&!date_tv.getText().toString().equals(getResources().getString(R.string.select_date)));
    }

}
