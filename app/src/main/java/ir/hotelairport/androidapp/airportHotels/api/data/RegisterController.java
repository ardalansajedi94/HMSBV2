package ir.hotelairport.androidapp.airportHotels.api.data;



import com.google.gson.JsonObject;

import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterController {
    private HotelApi.RegisterCallback registerCallback;

    public RegisterController(HotelApi.RegisterCallback registerCallback) {
        this.registerCallback = registerCallback;
    }


    public void start(JsonObject req){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hotelairport.ir/").addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<LoginRes> call = hotelApi.Register(req , "application/json" ,"application/json");
        call.enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                if (response.isSuccessful())
                    registerCallback.onResponse(response.body());
                else
                    registerCallback.onError( response.message() );

            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {

                try {
                    registerCallback.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
