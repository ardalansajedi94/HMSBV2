package ir.hotelairport.androidapp.airportHotels.api.data;

import com.google.gson.JsonObject;


import ir.hotelairport.androidapp.airportHotels.api.model.CheckStatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckBookStatusController {
    private HotelApi.CheckBookStatusCallBack checkBookStatus;

    public CheckBookStatusController(HotelApi.CheckBookStatusCallBack checkBookStatus) {
        this.checkBookStatus = checkBookStatus;
    }

    public void start(JsonObject req){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<CheckStatusResponse> call = hotelApi.CheckBookStatus(req , "application/json" ,"application/json");
        call.enqueue(new Callback<CheckStatusResponse>() {
            @Override
            public void onResponse(Call<CheckStatusResponse> call, Response<CheckStatusResponse> response) {
                if (response.isSuccessful())
                    checkBookStatus.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<CheckStatusResponse> call, Throwable t) {

                try {
                    checkBookStatus.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
