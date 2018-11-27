package ir.hotelairport.androidapp.airportHotels.api.data;


import com.google.gson.JsonObject;

import ir.hotelairport.androidapp.airportHotels.api.model.BookResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRoomController {
    private HotelApi.BookRoomCallBack bookRoomCallBack;

    public BookRoomController(HotelApi.BookRoomCallBack bookRoomCallBack) {
        this.bookRoomCallBack = bookRoomCallBack;
    }


    public void start(JsonObject req, String token) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(HotelApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi = retrofit.create(HotelApi.class);
        Call<BookResponse> call = hotelApi.BookRoom(req, "application/json", "application/json", token);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful())
                    bookRoomCallBack.onResponse(response.body());

            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {

                try {
                    bookRoomCallBack.onFailure(t.getCause().getMessage());
                } catch (NullPointerException e) {

                }

            }
        });
    }
}
