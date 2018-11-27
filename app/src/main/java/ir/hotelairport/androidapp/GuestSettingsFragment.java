package ir.hotelairport.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.util.Log;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.Locale;

import ir.hotelairport.androidapp.Models.Language;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;

import static android.content.Context.MODE_PRIVATE;



public class GuestSettingsFragment extends PreferenceFragmentCompat {

    private ListPreference languages_list;

    private DatabaseHandler db;
    SharedPreferences user_detail;

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.guest_settings_fragment, rootKey);
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, MODE_PRIVATE);
        db=new DatabaseHandler(getActivity());
        languages_list=(ListPreference)findPreference("change_lang_preference");
        languages_list.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"change_lng"));
        languages_list.setDialogTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"dialog_title_language"));
        DatabaseHandler db=new DatabaseHandler(getActivity());
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
        // additional setup
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferences
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);
    }


}
