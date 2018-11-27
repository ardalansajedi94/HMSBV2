package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.Locale;

import ir.hotelairport.androidapp.Models.Language;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerRequest;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    ProgressDialog progress;
    private ListPreference languages_list;
    private SwitchPreferenceCompat notification_preference;
    private Preference logout_pref;
    private String device_imei;
    SharedPreferences user_detail;
    private CharSequence languages[];
    private CharSequence languages_values[];
    private DatabaseHandler db;
    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
       PreferenceCategory preferenceCategory=(PreferenceCategory) findPreference("account_pf_cat");
        preferenceCategory.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"account"));
        logout_pref=findPreference("logout_pref");
        logout_pref.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"exit"));

        notification_preference=(SwitchPreferenceCompat) findPreference("notification_switch_preference");
        notification_preference.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"notifications"));
        logout_pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"exit_confirm_dlg")).setPositiveButton(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"yes"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Logout();
                    }
                })
                        .setNegativeButton(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"no"), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                return false;
            }
        });

        languages_list=(ListPreference)findPreference("change_lang_preference");
        languages_list.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"change_lng"));
        languages_list.setDialogTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"dialog_title_language"));
        final ArrayList<Language>supported_languages=db.getSupportedLanguages();
        CharSequence languages[]=new String[supported_languages.size()];
        CharSequence languages_values[]=new String[supported_languages.size()];
        for (int i=0;i<supported_languages.size();i++)
        {
            languages[i]=supported_languages.get(i).getName();
            languages_values[i]=String.valueOf(supported_languages.get(i).getId());
        }
        languages_list.setEntries(languages);
        languages_list.setEntryValues(languages_values);
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, MODE_PRIVATE);
        for (int i=0;i<supported_languages.size();i++)
        {
            if (user_detail.getInt(Constants.LANGUAGE_ID,1)==supported_languages.get(i).getId())
                languages_list.setValueIndex(i);
        }
        languages_list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("new_lang",newValue.toString());
                SharedPreferences.Editor editor = user_detail.edit();
                editor.putInt(Constants.LANGUAGE_ID,Integer.parseInt(newValue.toString()));
                String languageToLoad="";
                for (int i=0;i<supported_languages.size();i++)
                {
                    if (Integer.parseInt(newValue.toString())==supported_languages.get(i).getId())
                        languageToLoad=supported_languages.get(i).getLocale();
                }
                editor.putString(Constants.LANGUAGE_LOCALE,languageToLoad);
                editor.apply();
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config,
                        getActivity().getBaseContext().getResources().getDisplayMetrics());
                Intent i = getActivity().getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return false;
            }
        });
        notification_preference.setDefaultValue(true);
        notification_preference.setChecked(true);
        notification_preference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                change_notification_state((Boolean) newValue);
                return true;
            }
        });
        // additional setup
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferences
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
    }
    /*
    private void getSupportedLangugesList()
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
        Call<ServerResponse> response = requestInterface.(user_detail.getString(Constants.JWT,""));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            if (resp.getLanguages()!=null)
                            {
                                ArrayList<Language>supported_languages=resp.getLanguages();
                                List<String>language_names=new ArrayList<String>();
                                List<String>language_values_list=new ArrayList<String>();
                                for (int i=0;i<supported_languages.size();i++)
                                {
                                    language_names.add(supported_languages.get(i).getName());
                                    language_values_list.add(String.valueOf(supported_languages.get(i).getId()));
                                    languages=language_names.toArray(new CharSequence[language_names.size()]);
                                    languages_values=language_values_list.toArray(new CharSequence[language_values_list.size()]);
                                    languages_list.setEntries(languages);
                                    languages_list.setEntryValues(languages_values);
                                    languages_list.setValueIndex(user_detail.getInt(Constants.LANGUAGE,1)-1);
                                }
                            }
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(), getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
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

                Log.d("error:",t.getMessage());
            }
        });
    }*/
    private void change_notification_state(boolean notf_on)
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
        request.setDevice_id(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        if (notf_on)
            request.setNotf_on(1);
        else
            request.setNotf_on(0);
        Call<ServerResponse> response = requestInterface.change_notification_Settings(user_detail.getString(Constants.JWT,""),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Log.d("notf_change_message",resp.getMessage());
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(),db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"user_not_found"), Toast.LENGTH_SHORT).show();
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

                Log.d("error:",t.getMessage());
            }
        });
    }
    private void  Logout()
    {
        SharedPreferences user_detail;
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, MODE_PRIVATE);
        SharedPreferences.Editor editor = user_detail.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.JWT,null);
        editor.putString(Constants.USER_FIRST_NAME,null);
        editor.putString(Constants.USER_LAST_NAME,null);
        editor.apply();
        Intent i=new Intent(getActivity(),GuestActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        getActivity().finish();
    }


}
