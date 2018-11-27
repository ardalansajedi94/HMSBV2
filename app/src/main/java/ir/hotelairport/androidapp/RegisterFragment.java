package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

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
public class RegisterFragment extends Fragment {


    TextInputLayout name_til, last_name_til, n_id_til, address_til, phone_til, mobile_til, email_til, confirm_password_til, job_til, zip_code_til, password_til;
    TextInputEditText name_et, last_name_et, n_id_et, address_et, phone_et, mobile_et, email_et, confirm_password_et, job_et, zip_code_et, password_et;
    Button register_btn;
    ProgressDialog progress;
    private SharedPreferences user_detail;
    DatabaseHandler db;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initLayout(view);
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        db = new DatabaseHandler(getActivity());
        /*if (isRTL(Locale.getDefault()))
        {
            view.setRotationY(180);
        }*/
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (canRegister())
                    register_btn.setEnabled(true);
                else
                    register_btn.setEnabled(false);
                if (!confirm_password_et.getText().toString().trim().isEmpty()) {
                    if (!confirm_password_et.getText().toString().equals(password_et.getText().toString())) {
                        confirm_password_til.setError(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "not_same_pass"));
                        register_btn.setEnabled(false);
                    } else {
                        confirm_password_til.setError(null);
                        if (canRegister())
                            register_btn.setEnabled(true);
                    }

                }
            }
        };
        name_et.addTextChangedListener(textWatcher);
        last_name_et.addTextChangedListener(textWatcher);
        n_id_et.addTextChangedListener(textWatcher);
        address_et.addTextChangedListener(textWatcher);
        phone_et.addTextChangedListener(textWatcher);
        mobile_et.addTextChangedListener(textWatcher);
        email_et.addTextChangedListener(textWatcher);
        job_et.addTextChangedListener(textWatcher);
        zip_code_et.addTextChangedListener(textWatcher);
        password_et.addTextChangedListener(textWatcher);
        confirm_password_et.addTextChangedListener(textWatcher);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_et.getText().toString();
                String last_name = last_name_et.getText().toString();
                String n_id = n_id_et.getText().toString();
                String address = address_et.getText().toString();
                String phone = phone_et.getText().toString();
                String mobile = mobile_et.getText().toString();
                String email = email_et.getText().toString();
                String job = job_et.getText().toString();
                String zip_code = zip_code_et.getText().toString();
                String password = password_et.getText().toString();
                if (n_id.length() < 10) {
                    n_id_til.setError(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "national_id_error"));
                    n_id_et.requestFocus();
                } else if (password.length() < 6) {
                    password_til.setError(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "password_error"));
                    password_et.requestFocus();
                } else {
                    n_id_til.setError(null);
                    password_til.setError(null);
                    doRegister(name, last_name, n_id, address, phone, mobile, email, job, zip_code, password);
                }

            }
        });
        password_til.setPasswordVisibilityToggleEnabled(true);
        confirm_password_til.setPasswordVisibilityToggleEnabled(true);
        name_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "name"));
        last_name_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "last_name"));
        n_id_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "national_id"));
        address_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "address"));
        phone_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "phone"));
        mobile_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "mobile"));
        email_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "email"));
        password_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "password"));
        confirm_password_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "confirm_password"));
        job_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "job"));
        zip_code_til.setHint(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "zip_code"));
        register_btn.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "register"));
        return view;
    }

    private void doRegister(String name, String last_name, String n_id, String address, String phone, String mobile, String email, String job, String zip_code, String password) {
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
        request.setFirstname(name);
        request.setLastname(last_name);
        request.setNational_id(n_id);
        request.setAddress(address);
        request.setPhone(phone);
        request.setMobile(mobile);
        request.setEmail(email);
        request.setJob(job);
        request.setDevice_type(1);
        request.setDevice_id(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        request.setZip_code(zip_code);
        request.setPassword(password);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();
        request.setToken(status.getSubscriptionStatus().getUserId());
        Call<ServerResponse> response = requestInterface.register(request);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();

                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "register_success"), Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = user_detail.edit();
                            editor.putString(Constants.JWT, "Bearer " + resp.getJwt());
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

    private boolean canRegister() {
        return (!name_et.getText().toString().trim().isEmpty() && !last_name_et.getText().toString().trim().isEmpty() && !n_id_et.getText().toString().trim().isEmpty() && !address_et.getText().toString().trim().isEmpty() && !phone_et.getText().toString().trim().isEmpty() && !mobile_et.getText().toString().trim().isEmpty() && !email_et.getText().toString().trim().isEmpty() && !confirm_password_et.getText().toString().trim().isEmpty() && !job_et.getText().toString().trim().isEmpty() && !zip_code_et.getText().toString().trim().isEmpty() && !password_et.getText().toString().trim().isEmpty());
    }

    private void initLayout(View view) {
        name_til = (TextInputLayout) view.findViewById(R.id.name_TIL);
        last_name_til = (TextInputLayout) view.findViewById(R.id.last_name_TIL);
        n_id_til = (TextInputLayout) view.findViewById(R.id.national_id_TIL);
        address_til = (TextInputLayout) view.findViewById(R.id.address_TIL);
        phone_til = (TextInputLayout) view.findViewById(R.id.phone_TIL);
        mobile_til = (TextInputLayout) view.findViewById(R.id.mobile_TIL);
        email_til = (TextInputLayout) view.findViewById(R.id.email_TIL);
        confirm_password_til = (TextInputLayout) view.findViewById(R.id.confirm_password_TIL);
        job_til = (TextInputLayout) view.findViewById(R.id.job_TIL);
        zip_code_til = (TextInputLayout) view.findViewById(R.id.zip_code_TIL);
        password_til = (TextInputLayout) view.findViewById(R.id.password_TIL);

        name_et = (TextInputEditText) view.findViewById(R.id.name_ET);
        last_name_et = (TextInputEditText) view.findViewById(R.id.last_name_ET);
        n_id_et = (TextInputEditText) view.findViewById(R.id.national_id_ET);
        address_et = (TextInputEditText) view.findViewById(R.id.address_ET);
        phone_et = (TextInputEditText) view.findViewById(R.id.phone_ET);
        mobile_et = (TextInputEditText) view.findViewById(R.id.mobile_ET);
        email_et = (TextInputEditText) view.findViewById(R.id.email_ET);
        confirm_password_et = (TextInputEditText) view.findViewById(R.id.confirm_password_ET);
        job_et = (TextInputEditText) view.findViewById(R.id.job_ET);
        zip_code_et = (TextInputEditText) view.findViewById(R.id.zip_code_ET);
        password_et = (TextInputEditText) view.findViewById(R.id.password_ET);

        register_btn = (Button) view.findViewById(R.id.register_btn);
    }

}
