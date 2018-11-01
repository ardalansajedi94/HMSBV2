package ir.hotelairport.androidapp.airportHotels.api.data;



import com.google.gson.JsonObject;

import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginController {
    private HotelApi.LoginCallBack loginCallBack;

    public LoginController(HotelApi.LoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }


    public void start(JsonObject req){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hotelairport.ir/").addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<LoginRes> call = hotelApi.Login(req , "application/json" ,"application/json");
        call.enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                if (response.isSuccessful())
                    loginCallBack.onResponse(response.body());
                else
                    loginCallBack.onError( response.message() );

            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {

                try {
                    loginCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
