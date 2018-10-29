package net.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

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
public class LoginFragment extends Fragment  implements OSSubscriptionObserver  {
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    TextInputLayout username_til,password_til;
    TextInputEditText username_et,password_et;
    private String device_imei,user_name,password;
    Button login_btn,qr_login_btn;
    ProgressDialog progress;
    private DatabaseHandler db;
    private SharedPreferences user_detail;
    public LoginFragment() {
        // Required empty public constructor
    }



    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getSubscribed() &&
                stateChanges.getTo().getSubscribed()) {
            // get player ID
            stateChanges.getTo().getUserId();

        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        OneSignal.addSubscriptionObserver(this);
        username_til = (TextInputLayout) view.findViewById(R.id.user_name_TIL);
        password_til = (TextInputLayout) view.findViewById(R.id.password_TIL);
        username_et = (TextInputEditText) view.findViewById(R.id.user_name_ET);
        password_et = (TextInputEditText) view.findViewById(R.id.password_ET);
        login_btn=(Button)view.findViewById(R.id.login_btn);
        qr_login_btn=(Button)view.findViewById(R.id.qr_login_btn);
        username_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!username_et.getText().toString().trim().isEmpty()&&!password_et.getText().toString().trim().isEmpty())
                    login_btn.setEnabled(true);
                else
                    login_btn.setEnabled(false);

            }
        });
        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!username_et.getText().toString().trim().isEmpty()&&!password_et.getText().toString().trim().isEmpty())
                    login_btn.setEnabled(true);
                else
                    login_btn.setEnabled(false);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                user_name=username_et.getText().toString();
                password=password_et.getText().toString();
                if (user_name.length()<10)
                    username_til.setError(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"national_id_error"));
                else if (password.length()<6)
                    password_til.setError(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"password_error"));
                else
                {
                    username_til.setError(null);
                    password_til.setError(null);
                    doLogin();

                }


            }
        });
        qr_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qr_code","clicked");
                hideKeyboard();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new QrCodeLoginFragment());
                fragmentTransaction.addToBackStack("QRFragment");
                fragmentTransaction.commit();
            }
        });
        /*if (isRTL(Locale.getDefault()))
        {
            view.setRotationY(180);
        }*/

        username_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"national_id"));
        password_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"password"));
        login_btn.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"login"));
        qr_login_btn.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"qr_login"));
        return  view;
    }

    private void doLogin()
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
        ServerRequest request = new ServerRequest();
        request.setUsername(this.user_name);
        request.setPassword(password);
        request.setDevice_type(1);
        request.setDevice_id(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();
        request.setToken(status.getSubscriptionStatus().getUserId());
        Call<ServerResponse> response = requestInterface.login(request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();

                    switch (response.code()) {
                        case 200:
                            if (resp != null) {
                                SharedPreferences.Editor editor = user_detail.edit();
                                editor.putString(Constants.JWT, "Bearer "+resp.getJwt());
                                editor.putBoolean(Constants.IS_LOGGED_IN, true);
                                editor.putString(Constants.USER_FIRST_NAME, resp.getProfile().getFirstname());
                                editor.putString(Constants.USER_LAST_NAME, resp.getProfile().getLastname());
                                editor.putString(Constants.PROFILE_IMAGE_NAME, resp.getProfile().getProfile_image());
                                editor.putInt(Constants.ROOM_NO, resp.getProfile().getRoom_no());
                                editor.apply();
                                Intent i = new Intent(getActivity(), LoggedInActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                            break;
                        case 401:
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"user_not_found"), Toast.LENGTH_SHORT).show();
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
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
