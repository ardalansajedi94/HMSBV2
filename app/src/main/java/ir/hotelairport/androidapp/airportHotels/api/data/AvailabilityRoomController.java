package ir.hotelairport.androidapp.airportHotels.api.data;

import com.google.gson.JsonObject;


import ir.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvailabilityRoomController {
    private HotelApi.AvailabilityRoomCallBack availabilityRoomCallBack;

    public AvailabilityRoomController(HotelApi.AvailabilityRoomCallBack availabilityRoomCallBack) {
        this.availabilityRoomCallBack = availabilityRoomCallBack;
    }

    public void start(JsonObject req , String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<AvailabilityRes> call = hotelApi.AvailabilityRoom(req , "application/json" ,"application/json" , token );
        call.enqueue(new Callback<AvailabilityRes>() {
            @Override
            public void onResponse(Call<AvailabilityRes> call, Response<AvailabilityRes> response) {
                if (response.isSuccessful())
                    availabilityRoomCallBack.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<AvailabilityRes> call, Throwable t) {

                try {
                    availabilityRoomCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
