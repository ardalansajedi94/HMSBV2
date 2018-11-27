package ir.hotelairport.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ir.hotelairport.androidapp.Models.HotelSetting;
import ir.hotelairport.androidapp.Models.Language;
import ir.hotelairport.androidapp.Models.LanguageKey;
import ir.hotelairport.androidapp.Models.Position;
import ir.hotelairport.androidapp.Models.Translation;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences user_detail;
    TextView initingAppTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initingAppTv=(TextView)findViewById(R.id.initingappTv);
        user_detail=getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        if(user_detail.getInt(Constants.APP_SERVER_INIT,0)==0)
        {
            initingAppTv.setVisibility(View.GONE);
            InitAppFromServer();
            AppLunchProtocol();
        }
        else
        {
            initingAppTv.setVisibility(View.GONE);
            AppLunchProtocol();
        }

    }
    private void InitAppFromServer()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response ;
        response = requestInterface.InitAppFromServer();
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            ArrayList<Language> languages=resp.getLanguages();
                            ArrayList<LanguageKey> languageKeys=resp.getLanguage_keys();
                            ArrayList<Translation> translations=resp.getTranslations();
                            Position position=resp.getHotel_position();
                            DatabaseHandler db=new DatabaseHandler(SplashActivity.this);
                            for (int i=0;i<languages.size();i++)
                            {
                                db.addToSupportedLanguages(languages.get(i));
                            }
                            for (int i=0;i<languageKeys.size();i++)
                            {
                                db.addToLanguageKeys(languageKeys.get(i).getId(),languageKeys.get(i).getKey());
                            }
                            for (int i=0;i<translations.size();i++)
                            {
                                db.addNewTranslation(translations.get(i));
                            }
                            db.addToPositionTable(position);
                            ArrayList<HotelSetting>settings=new ArrayList<HotelSetting>();
                            settings=resp.getSettings();
                            for (int i=0;i<settings.size();i++)
                            {
                                db.addToSettingsTable(settings.get(i));
                            }
                            SharedPreferences.Editor editor = user_detail.edit();
                            editor.putInt(Constants.APP_SERVER_INIT, 1);
                            editor.apply();
                          //  initingAppTv.setVisibility(View.GONE);
                         //   AppLunchProtocol();


                        }
                        break;
                    default:

                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("error:",t.getMessage());
            }
        });
    }
    private void AppLunchProtocol()
    {
        if (user_detail.getInt(Constants.LANGUAGE_ID,-1)==-1)
        {
            SharedPreferences.Editor editor = user_detail.edit();
            editor.putInt(Constants.LANGUAGE_ID, 1);
            editor.putString(Constants.LANGUAGE_LOCALE,"fa");
            editor.apply();
            Locale locale = new Locale("fa");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
        else
        {
            String languageToLoad=user_detail.getString(Constants.LANGUAGE_LOCALE,"en");
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }

        if (user_detail.getBoolean(Constants.IS_LOGGED_IN, false)) {
            Intent i = new Intent(this, LoggedInActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
        else
        {
            Intent i = new Intent(this, GuestActivity.class);
            startActivity(i);
            SplashActivity.this.finish();
        }
    }
}
