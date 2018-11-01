package ir.hotelairport.androidapp.airportHotels.api.data;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoucherController {
    private HotelApi.VoucherCallBack loginCallBack;

    public VoucherController(HotelApi.VoucherCallBack voucherCallBack) {
        this.loginCallBack = voucherCallBack;
    }


    public void start(String auth , String bookId){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hotelairport.ir/voucher/").addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<ResponseBody> call = hotelApi.Voucher(auth , bookId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                    loginCallBack.onResponse(response.body());
                else
                    loginCallBack.onError( response.message() );

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    loginCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
