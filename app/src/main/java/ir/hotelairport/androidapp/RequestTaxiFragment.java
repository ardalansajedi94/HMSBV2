package ir.hotelairport.androidapp;
//
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
////import com.google.android.gms.maps.CameraUpdateFactory;
////import com.google.android.gms.maps.GoogleMap;
////import com.google.android.gms.maps.MapView;
////import com.google.android.gms.maps.MapsInitializer;
////import com.google.android.gms.maps.OnMapReadyCallback;
////import com.google.android.gms.maps.model.CameraPosition;
////import com.google.android.gms.maps.model.LatLng;
////import com.google.android.gms.maps.model.Marker;
////import com.google.android.gms.maps.model.MarkerOptions;
//import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
//import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
//import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
//import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//import ir.hotelsys.androidapp.Models.Position;
//import ir.hotelsys.androidapp.SQLiteDB.DatabaseHandler;
//import ir.hotelsys.androidapp.Server.RequestInterface;
//import ir.hotelsys.androidapp.Server.ServerRequest;
//import ir.hotelsys.androidapp.Server.ServerResponse;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class RequestTaxiFragment extends Fragment implements
//        TimePickerDialog.OnTimeSetListener,
//        DatePickerDialog.OnDateSetListener
//{
//    Button date_tv, time_tv;
//    Spinner car_type_spinner;
//    EditText address_et;
//    Button send_req_btn;
//    ProgressDialog progress;
//    String selected_car;
////    MapView mMapView;
////    private GoogleMap googleMap;
//    private SharedPreferences user_detail;
//    private DatabaseHandler db;
//    private double lat,lng;
//    private static final String TIMEPICKER = "TimePickerDialog", DATEPICKER = "DatePickerDialog";
//    public RequestTaxiFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
//        String time =  hourOfDay + ":" + minute;
//        time_tv.setText(time);
//        send_req_btn.setEnabled(SubmitCondition());
//    }
//
//    @Override
//    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        String date= year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
//        date_tv.setText(date);
//        send_req_btn.setEnabled(SubmitCondition());
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
////        mMapView.onResume();
//        send_req_btn.setEnabled(SubmitCondition());
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_request_taxi, container, false);
//        db=new DatabaseHandler(getActivity());
//        send_req_btn = (Button) view.findViewById(R.id.send_req_btn);
//        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"taxi"));
//        date_tv = (Button) view.findViewById(R.id.date_tv);
//        time_tv = (Button) view.findViewById(R.id.time_tv);
//        car_type_spinner = (Spinner) view.findViewById(R.id.car_type_spinner);
//        address_et = (EditText) view.findViewById(R.id.address_et);
//        List<String> carTypes = new ArrayList<String>();
//        carTypes.add(getResources().getString(R.string.sedan));
//        carTypes.add(getResources().getString(R.string.van));
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carTypes);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        address_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                send_req_btn.setEnabled(SubmitCondition());
//            }
//        });
//        car_type_spinner.setAdapter(dataAdapter);
//        car_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i==0)
//                {
//                    selected_car=getResources().getString(R.string.sedan_key);
//                }
//                else
//                {
//                    selected_car=getResources().getString(R.string.van_key);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        time_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                send_req_btn.setEnabled(SubmitCondition());
//                if (user_detail.getInt(Constants.LANGUAGE_ID,0)==1)
//                {
//
//                    PersianCalendar now = new PersianCalendar();
//                    TimePickerDialog tpd = TimePickerDialog.newInstance(
//                            RequestTaxiFragment.this,
//                            now.get(PersianCalendar.HOUR_OF_DAY),
//                            now.get(PersianCalendar.MINUTE),
//                            true
//                    );
//                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialogInterface) {
//                            Log.d(TIMEPICKER, "Dialog was cancelled");
//                        }
//                    });
//                    tpd.show(getActivity().getFragmentManager(), TIMEPICKER);
//                }
//                else
//                {
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    android.app.TimePickerDialog mTimePicker;
//                    mTimePicker = new android.app.TimePickerDialog(getActivity(), new android.app.TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            String time =  String.valueOf(selectedHour) + ":" + String.valueOf(selectedHour);
//                            time_tv.setText(time);
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.show();
//                }
//                send_req_btn.setEnabled(SubmitCondition());
//            }
//        });
//        date_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                send_req_btn.setEnabled(SubmitCondition());
//                if (user_detail.getInt(Constants.LANGUAGE_ID,1)==1) {
//                    PersianCalendar now = new PersianCalendar();
//                    DatePickerDialog dpd = DatePickerDialog.newInstance(
//                            RequestTaxiFragment.this,
//                            now.getPersianYear(),
//                            now.getPersianMonth(),
//                            now.getPersianDay()
//                    );
//                    dpd.show(getActivity().getFragmentManager(), DATEPICKER);
//                }
//                else
//                {
//                    final Calendar c = Calendar.getInstance();
//                    int year = c.get(Calendar.YEAR);
//                    int month = c.get(Calendar.MONTH);
//                    int day = c.get(Calendar.DAY_OF_MONTH);
//                    android.app.DatePickerDialog dp = new  android.app.DatePickerDialog(getActivity(),new  android.app.DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            String date= year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
//                            date_tv.setText(date);
//
//                        }
//                    },year,month,day);
//                    dp.show();
//
//                }
//                send_req_btn.setEnabled(SubmitCondition());
//            }
//        });
//        send_req_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendRequest(time_tv.getText().toString(),date_tv.getText().toString(),address_et.getText().toString(),selected_car,lat,lng);
//            }
//        });
//        mMapView = (MapView) view.findViewById(R.id.mapView);
//        mMapView.onCreate(savedInstanceState);
//
//        mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//                // For showing a move to my location button
//                //    googleMap.setMyLocationEnabled(true);
//
//                // For dropping a marker at a point on the Map
//                DatabaseHandler db =new DatabaseHandler(getActivity());
//                Position hoteLatLng=db.getHotelPosition();
//                LatLng Hotel = new LatLng(hoteLatLng.getLat(), hoteLatLng.getLng());
//
//                 googleMap.addMarker(new MarkerOptions().position(Hotel).title(db.getHOtelInfoForLang(user_detail.getInt(Constants.LANGUAGE_ID,1)).getHotel_name()).snippet(db.getHOtelInfoForLang(user_detail.getInt(Constants.LANGUAGE_ID,1)).getHotel_name()));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(Hotel).zoom(12).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//                    @Override
//                    public void onMarkerDragStart(Marker arg0) {
//                        // TODO Auto-generated method stub
//                        Log.d("System out", "onMarkerDragStart..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
//                    }
//
//                    @SuppressWarnings("unchecked")
//                    @Override
//                    public void onMarkerDragEnd(Marker arg0) {
//                        // TODO Auto-generated method stub
//                        Log.d("System out", "onMarkerDragEnd..."+arg0.getPosition().latitude+"..."+arg0.getPosition().longitude);
//
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
//                    }
//
//                    @Override
//                    public void onMarkerDrag(Marker arg0) {
//                        // TODO Auto-generated method stub
//                        Log.i("System out", "onMarkerDrag...");
//                    }
//                });
//                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//                    @Override
//                    public void onCameraIdle() {
//                        LatLng current_latlng=googleMap.getCameraPosition().target;
//                        lat=current_latlng.latitude;
//                        lng=current_latlng.longitude;
//                        address_et.setText(getAddress(current_latlng.latitude,current_latlng.longitude));
//                    }
//                });
//                googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
//                    @Override
//                    public void onCameraMoveStarted(int reason) {
//
//                        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
//
//                        } else if (reason == GoogleMap.OnCameraMoveStartedListener
//                                .REASON_API_ANIMATION) {
//
//                        } else if (reason == GoogleMap.OnCameraMoveStartedListener
//                                .REASON_DEVELOPER_ANIMATION) {
//
//                        }
//                    }
//                });
//                googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//                    @Override
//                    public void onCameraMove() {
//
//                    }
//                });
//                googleMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
//                    @Override
//                    public void onCameraMoveCanceled() {
//
//                    }
//                });
//
////Don't forget to Set draggable(true) to marker, if this not set marker does not drag.
//
//                // googleMap.addMarker(new MarkerOptions().position(tehran).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)).draggable(true).visible(true));
//            }
//        });
//        return view;
//    }
//    private void sendRequest(String time,String date,String address,String car_type,double lat,double lng)
//    {
//        address=address.replaceAll("[\n\r]", "");
//        progress = new ProgressDialog(getActivity());
//        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
//        ServerRequest request = new ServerRequest();
//        request.setTime(time);
//        request.setDate(date);
//        request.setAddress(address);
//        request.setCar(car_type);
//        request.setCount(1);
//        request.setLat(lat);
//        request.setLng(lng);
//        Call<ServerResponse> response = requestInterface.send_taxi_request(user_detail.getString(Constants.JWT,""),request);
//        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
//                progress.dismiss();
//                ServerResponse resp = response.body();
//                Log.d("response",String.valueOf(response.code()));
//                switch (response.code()) {
//                    case 200:
//                        if (resp != null) {
//                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"send_success"), Toast.LENGTH_SHORT).show();
//                            getActivity().onBackPressed();
//                        }
//                        break;
//                    case 401:
//                        Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"not_allowed_user"), Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        if (resp != null) {
//                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                progress.dismiss();
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("error:",t.getMessage());
//            }
//        });
//
//    }
//    private Boolean SubmitCondition()
//    {
//        return (!time_tv.getText().toString().equals(getResources().getString(R.string.time))&&!date_tv.getText().toString().equals(getResources().getString(R.string.select_date))&&!address_et.getText().toString().trim().equals(""));
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mMapView.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mMapView.onLowMemory();
//    }
//    private String getAddress(double latitude, double longitude) {
//        StringBuilder result = new StringBuilder();
//        try {
//            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                result.append(address.getLocality()).append("-");
//                result.append(address.getFeatureName());
//            }
//        } catch (IOException e) {
//            Log.e("tag", e.getMessage());
//        }
//
//        return result.toString();
//    }
//}
