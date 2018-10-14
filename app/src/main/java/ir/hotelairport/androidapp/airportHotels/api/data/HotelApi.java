package ir.hotelairport.androidapp.airportHotels.api.data;



import com.google.gson.JsonObject;


import java.util.List;

import ir.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;
import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;
import ir.hotelairport.androidapp.airportHotels.api.model.BookResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.CheckStatusResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.GetApi;
import ir.hotelairport.androidapp.airportHotels.api.model.ProfileResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.ReserveRes;
import ir.hotelairport.androidapp.airportHotels.api.model.ServicesResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.Voucher;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface HotelApi {
    String BASE_URL = "https://hotelairport.ir/api/v1/haw/";




    @POST("availability")
    Call<AvailabilityRes> AvailabilityRoom(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface AvailabilityRoomCallBack{
        void onResponse(AvailabilityRes res);
        void onFailure(String cause);
    }
    @POST("service")
    Call<ServicesResponse> ServiceList(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface ServiceListCallBack{
        void onResponse(ServicesResponse res);
        void onFailure(String cause);
    }


    @POST("reserve")
    Call<ReserveRes> ReserveRoom(@Body JsonObject req , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface ReserveRoomCallBack{
        void onResponse(ReserveRes res);
        void onFailure(String cause);
    }

    @POST("book")
    Call<BookResponse> BookRoom(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface BookRoomCallBack {
        void onResponse(BookResponse url);
        void onFailure(String cause);
    }
    @GET("showProfile")
    Call<ProfileResponse> ShowProfile(@Header("Accept") String a , @Header("Content-Type") String c, @Header("Authorization") String auth );
    interface ShowProfile {
        void onResponse(ProfileResponse response);
        void onFailure(String cause);
        void onError(String cause);
    }
    @POST("bookService")
    Call<BookResponse> BookService(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface BookServiceCallBack {
        void onResponse(BookResponse url);
        void onFailure(String cause);
    }
    @POST("checkBookStatus")
    Call<CheckStatusResponse> CheckBookStatus(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c,@Header("Authorization") String auth );
    interface CheckBookStatusCallBack{
        void onResponse(CheckStatusResponse response);
        void onFailure(String cause);
    }
    @POST("api/v1/register")
    Call<LoginRes> Register(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c );
    interface RegisterCallback{
        void onResponse(LoginRes response);
        void onFailure(String cause);
        void onError(String cause);
    }
    @POST("oauth/token")
    Call<LoginRes> Login(@Body JsonObject req  , @Header("Accept") String a , @Header("Content-Type") String c );
    interface LoginCallBack{
        void onResponse(LoginRes response);
        void onFailure(String cause);
        void onError(String cause);
    }
    @Streaming
    @GET
    Call<ResponseBody> Voucher(@Header("Authorization") String auth , @Url String url);
    interface VoucherCallBack{
        void onResponse(ResponseBody responseBody);
        void onFailure(String cause);
        void onError(String cause);
    }
    @GET("purchase-list")
    Call<List<Voucher>> PurchaseList(@Header("Authorization") String auth );
    interface PurchaseListCallBack{
        void onResponse(List<Voucher> voucherList);
        void onFailure(String cause);
        void onError(String cause);
    }

}
