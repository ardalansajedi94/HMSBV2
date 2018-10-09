package ir.hotelairport.androidapp.airportHotels.api.data;



import com.google.gson.JsonObject;


import ir.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;
import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;
import ir.hotelairport.api.model.BookResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.CheckStatusResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.GetApi;
import ir.hotelairport.androidapp.airportHotels.api.model.ReserveRes;
import ir.hotelairport.androidapp.airportHotels.api.model.ServicesResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HotelApi {
    String BASE_URL = "https://hotelairport.ir/api/v1/haw/";




    @POST("availability")
    Call<AvailabilityRes> AvailabilityRoom(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c);
    interface AvailabilityRoomCallBack{
        void onResponse(AvailabilityRes res);
        void onFailure(String cause);
    }
    @POST("service")
    Call<ServicesResponse> ServiceList(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c);
    interface ServiceListCallBack{
        void onResponse(ServicesResponse res);
        void onFailure(String cause);
    }


    @POST("reserve")
    Call<ReserveRes> ReserveRoom(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c);
    interface ReserveRoomCallBack{
        void onResponse(ReserveRes res);
        void onFailure(String cause);
    }

    @POST("book")
    Call<BookResponse> BookRoom(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c);
    interface BookRoomCallBack {
        void onResponse(BookResponse url);
        void onFailure(String cause);
    }
    @POST("bookService")
    Call<BookResponse> BookService(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c);
    interface BookServiceCallBack {
        void onResponse(BookResponse url);
        void onFailure(String cause);
    }
    @POST("checkBookStatus")
    Call<CheckStatusResponse> CheckBookStatus(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c);
    interface CheckBookStatusCallBack{
        void onResponse(CheckStatusResponse response);
        void onFailure(String cause);
    }
    @POST("api/v1/register")
    Call<LoginRes> Register(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c);
    interface RegisterCallback{
        void onResponse(LoginRes response);
        void onFailure(String cause);
        void onError(String cause);
    }
    @POST("oauth/token")
    Call<LoginRes> Login(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c);
    interface LoginCallBack{
        void onResponse(LoginRes response);
        void onFailure(String cause);
        void onError(String cause);
    }


}
