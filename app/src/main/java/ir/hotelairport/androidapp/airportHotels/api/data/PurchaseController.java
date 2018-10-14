package ir.hotelairport.androidapp.airportHotels.api.data;


import java.util.List;

import ir.hotelairport.androidapp.airportHotels.api.model.Voucher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PurchaseController {
    private HotelApi.PurchaseListCallBack purchaseListCallBack;

    public PurchaseController(HotelApi.PurchaseListCallBack purchaseListCallBack) {
        this.purchaseListCallBack = purchaseListCallBack;
    }


    public void start(String auth){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hotelairport.ir/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<List<Voucher>> call = hotelApi.PurchaseList(auth);
        call.enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.isSuccessful())
                    purchaseListCallBack.onResponse(response.body());
                else
                    purchaseListCallBack.onError( response.message() );

            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {

                try {
                    purchaseListCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
