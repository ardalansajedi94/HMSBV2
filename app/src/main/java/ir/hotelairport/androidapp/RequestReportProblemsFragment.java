package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
public class RequestReportProblemsFragment extends Fragment {


    private Spinner service_spinner,service_type_spinner;
    private String selected_service,selected_service_type;
    private EditText explanation_et;
    private Button send_btn;
    private List<String> services_type;
    private List<String>services_type_key;
    private ArrayAdapter<String> ServicesTypeArrayAdapter;
    ProgressDialog progress;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    public RequestReportProblemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_request_report_problems, container, false);
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        db=new DatabaseHandler(getActivity());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"problems"));

        service_spinner=(Spinner)view.findViewById(R.id.service_spinner);
        service_type_spinner=(Spinner)view.findViewById(R.id.service_type_spinner);
        explanation_et =(EditText)view.findViewById(R.id.note_et);
        send_btn = (Button)view.findViewById(R.id.send_req_btn);
        List<String> services = new ArrayList<String>();
        services.add(getResources().getString(R.string.room_air));
        services.add(getResources().getString(R.string.water));
        services.add(getResources().getString(R.string.shiralat));
        services.add(getResources().getString(R.string.gereftegi));
        services.add(getResources().getString(R.string.sifun));
        services.add(getResources().getString(R.string.barg));
        services.add(getResources().getString(R.string.lamp));
        ArrayAdapter<String> serviceDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services);
        serviceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_spinner.setAdapter(serviceDataAdapter);
        service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        selected_service=getResources().getString(R.string.room_air_key);
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();
                        services_type.add(getResources().getString(R.string.lack_of_proper_cold));
                        services_type_key.add(getResources().getString(R.string.lack_of_proper_cold_key));

                        services_type.add(getResources().getString(R.string.lack_of_proper_heat));
                        services_type_key.add(getResources().getString(R.string.lack_of_proper_heat_key));

                        services_type.add(getResources().getString(R.string.tahvie_bad));
                        services_type_key.add("inappropriate-air-conditioning");

                        services_type.add(getResources().getString(R.string.smell_smoke));
                        services_type_key.add("smell-smoke");

                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                    case 1:
                        selected_service=getResources().getString(R.string.water_key);
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();
                        services_type.add(getResources().getString(R.string.low_cold_water_pressure));
                        services_type_key.add(getResources().getString(R.string.low_cold_water_pressure_key));
                        services_type.add(getResources().getString(R.string.low_hot_water_pressure));
                        services_type_key.add(getResources().getString(R.string.low_hot_water_pressure_key));
                        services_type.add(getResources().getString(R.string.abe_garm_pb));
                        services_type_key.add("inappropriate-temperature-of-hot-water");
                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                    case 2:
                        selected_service="valves-crash";
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();

                        services_type.add(getResources().getString(R.string.washstand));
                        services_type_key.add("washstand");

                        services_type.add(getResources().getString(R.string.bathtub));
                        services_type_key.add("bathtub");

                        services_type.add(getResources().getString(R.string.bathroom));
                        services_type_key.add("bathroom");

                        services_type.add(getResources().getString(R.string.wc));
                        services_type_key.add("wc");

                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                    case 3:
                        selected_service="water-way-blocked";
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();

                        services_type.add(getResources().getString(R.string.washstand));
                        services_type_key.add("washstand");

                        services_type.add(getResources().getString(R.string.bathtub));
                        services_type_key.add("bathtub");

                        services_type.add(getResources().getString(R.string.bathroom));
                        services_type_key.add("bathroom");

                        services_type.add(getResources().getString(R.string.wc));
                        services_type_key.add("wc");

                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                    case 4:
                        selected_service="siphon-breakdown";
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();
                        services_type.add(getResources().getString(R.string.sifun));
                        services_type_key.add("siphon-breakdown");
                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                    case 5:
                        selected_service="electric-failure";

                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"tv"));
                        services_type_key.add("tv");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"receiver"));
                        services_type_key.add("receiver");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"tea_maker"));
                        services_type_key.add("tea-maker");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"coffee_maker"));
                        services_type_key.add("coffee-maker");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"phone"));
                        services_type_key.add("phone");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"entrance_door"));
                        services_type_key.add("entrance-door");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"outlet_or_key_breakdown"));
                        services_type_key.add("outlet-or-key-breakdown");

                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);

                        break;
                    case 6:
                        selected_service="replacing-the-burnt-bulb";
                        services_type=new ArrayList<String>();
                        services_type_key=new ArrayList<String>();

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"room"));
                        services_type_key.add("room");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"wc"));
                        services_type_key.add("wc");

                        services_type.add(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"lighthouse"));
                        services_type_key.add("lighthouse");
                        ServicesTypeArrayAdapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services_type);
                        ServicesTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        service_type_spinner.setAdapter(ServicesTypeArrayAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        service_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_service_type=services_type_key.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest(selected_service,selected_service_type,explanation_et.getText().toString());
            }
        });
        return view;
    }
    private void sendRequest(String service,String type,String note)
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
        request.setProblems(service);
        request.setType(type);
        request.setExplanation(note);
        Call<ServerResponse> response = requestInterface.send_problems_request(user_detail.getString(Constants.JWT,""),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>(){
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
                        Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"not_allowed_user"), Toast.LENGTH_SHORT).show();
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

}
