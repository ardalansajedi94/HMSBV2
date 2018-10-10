package ir.hotelairport.androidapp.airportHotels.api.data;

import com.google.gson.JsonObject;


import ir.hotelairport.androidapp.airportHotels.api.model.ServicesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceListController {
    private HotelApi.ServiceListCallBack serviceListCallBack;

    public ServiceListController(HotelApi.ServiceListCallBack serviceListCallBack) {
        this.serviceListCallBack = serviceListCallBack;
    }

    public void start(JsonObject req,String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<ServicesResponse> call = hotelApi.ServiceList(req , "application/json" ,"application/json" ,token);
        call.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {
                if (response.isSuccessful())
                    serviceListCallBack.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {

                try {
                    serviceListCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
