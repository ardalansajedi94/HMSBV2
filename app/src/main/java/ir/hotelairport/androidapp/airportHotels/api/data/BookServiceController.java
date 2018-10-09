package ir.hotelairport.androidapp.airportHotels.api.data;



import com.google.gson.JsonObject;

import ir.hotelairport.api.model.BookResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookServiceController {
    private HotelApi.BookServiceCallBack bookServiceCallBack;

    public BookServiceController(HotelApi.BookServiceCallBack bookServiceCallBack) {
        this.bookServiceCallBack = bookServiceCallBack;
    }


    public void start(JsonObject req){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<BookResponse> call = hotelApi.BookService(req , "application/json" ,"application/json");
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful())
                    bookServiceCallBack.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {

                try {
                    bookServiceCallBack.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
