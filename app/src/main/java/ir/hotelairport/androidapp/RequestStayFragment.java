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
public class RequestStayFragment extends Fragment {


    private Spinner service_spinner;
    List<String> services_keys;
    private String selected_service;
    private EditText explanation_et;
    private Button send_btn;
    ProgressDialog progress;
    private SharedPreferences user_detail;
    DatabaseHandler db;

    public RequestStayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_stay, container, false);
        db = new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "stay"));
        service_spinner = (Spinner) view.findViewById(R.id.service_spinner);
        explanation_et = (EditText) view.findViewById(R.id.note_et);
        send_btn = (Button) view.findViewById(R.id.send_req_btn);
        List<String> services = new ArrayList<String>();
        services_keys = new ArrayList<String>();
        services.add(getResources().getString(R.string.extend_stay));
        services_keys.add(getResources().getString(R.string.extend_stay_key));
        services.add(getResources().getString(R.string.room_reserve));
        services_keys.add(getResources().getString(R.string.room_reserve_key));
        ArrayAdapter<String> serviceDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, services);
        serviceDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_spinner.setAdapter(serviceDataAdapter);
        service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_service = services_keys.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(selected_service, explanation_et.getText().toString());
            }
        });
        return view;
    }

    private void sendRequest(String service, String note) {
        if (note.trim().equals(""))
            note = "\u00A0";
        note = note.replaceAll("[\n\r]", "");
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
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
        request.setRequest_type(service);
        request.setExplanation(note);
        Call<ServerResponse> response = requestInterface.send_stay_request(user_detail.getString(Constants.JWT, ""), request);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                Log.d("response", String.valueOf(response.code()));
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "send_success"), Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "not_allowed_user"), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:", t.getMessage());
            }
        });

    }

}
