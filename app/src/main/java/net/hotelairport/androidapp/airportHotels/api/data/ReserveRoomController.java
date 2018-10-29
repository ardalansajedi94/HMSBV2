package net.hotelairport.androidapp.airportHotels.api.data;

import com.google.gson.JsonObject;


import net.hotelairport.androidapp.airportHotels.api.model.ReserveRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReserveRoomController {
    private HotelApi.ReserveRoomCallBack reserveRoomCallBack;

    public ReserveRoomController(HotelApi.ReserveRoomCallBack reserveRoomCallBack) {
        this.reserveRoomCallBack = reserveRoomCallBack;
    }

    public void start(JsonObject req , String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<ReserveRes> call = hotelApi.ReserveRoom(req , "application/json" ,"application/json" , token);
        call.enqueue(new Callback<ReserveRes>() {
            @Override
            public void onResponse(Call<ReserveRes> call, Response<ReserveRes> response) {
                if (response.isSuccessful())
                    reserveRoomCallBack.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<ReserveRes> call, Throwable t) {

                try {
                    reserveRoomCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
