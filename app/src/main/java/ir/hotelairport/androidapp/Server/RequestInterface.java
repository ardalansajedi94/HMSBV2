package ir.hotelairport.androidapp.Server;

import java.util.Map;

import ir.hotelairport.androidapp.Models.Service;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Mohammad on 8/31/2017.
 */

public interface RequestInterface {
    @POST("guest/login")
    Call<ServerResponse> login(@Body ServerRequest request);

    @POST("guest/qrLogin")
    Call<ServerResponse> qrLogin(@Body ServerRequest request);

    @GET("city_information")
    Call<ServerResponse> city_info(@Query("cat_id") int cat_id, @Query("lang_id") int lang_id);

    @GET("information")
    Call<ServerResponse> hotel_info(@Query("cat_id") int cat_id, @Query("lang_id") int lang_id);

    @GET("news")
    Call<ServerResponse> hotel_news(@Query("lang_id") int lang_id);

    @GET
    Call<ServerResponse> dynamic_url(@Url String url);


    @Headers("Accept: application/json")
    @POST("guest/taxi-request")
    Call<ServerResponse> send_taxi_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/clothes")
    Call<ServerResponse> send_clothes_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/house-keeping")
    Call<ServerResponse> send_house_keeping_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/wake-up")
    Call<ServerResponse> send_wake_up_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/stay")
    Call<ServerResponse> send_stay_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/problems")
    Call<ServerResponse> send_problems_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/feedback")
    Call<ServerResponse> send_feedback(@Header("Authorization") String jwt, @Body ServerRequest request);

    @GET("time_line")
    Call<ServerResponse> get_time_line(@Header("Authorization") String jwt, @Query("lang_id") int lang_id);

    @Headers("Accept: application/json")
    @POST("guest/register")
    Call<ServerResponse> register(@Body ServerRequest request);

    @GET("restaurants")
    Call<ServerResponse> get_restaurants(@Header("Authorization") String jwt, @Query("lang_id") int lang_id);

    @GET("coffeeShops")
    Call<ServerResponse> get_coffeshops(@Header("Authorization") String jwt, @Query("lang_id") int lang_id);

    @GET
    Call<ServerResponse> dynamic_url_with_jwt(@Header("Authorization") String jwt, @Url String url);

    @Headers("Accept: application/json")
    @POST("guest/basket")
    Call<ServerResponse> order(@Header("Authorization") String jwt, @Body ServerRequest request);

    @GET("guest/basket")
    Call<ServerResponse> get_basket(@Header("Authorization") String jwt);

    @POST("guest/basket/submit")
    Call<ServerResponse> submit_basket(@Header("Authorization") String jwt);

    @Headers("Accept: application/json")
    @POST("guest/minibar")
    Call<ServerResponse> send_charge_minibar_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @POST("guest/reserve")
    Call<ServerResponse> send_reserve_table_request(@Header("Authorization") String jwt, @Body ServerRequest request);

    @GET("guest/profile")
    Call<ServerResponse> get_profile(@Header("Authorization") String jwt, @Query("lang_id") int lang_id);

    @Multipart
    @Headers("Accept: application/json")
    @POST("guest/profile/picture")
    Call<ServerResponse> upload_profile_picture(@Header("Authorization") String jwt, @Part MultipartBody.Part file);

    @Headers("Accept: application/json")
    @POST("guest/invoice")
    Call<ServerResponse> request_invoice(@Header("Authorization") String jwt);

    @Headers("Accept: application/json")
    @POST()
    Call<ServerResponse> dynamic_post_request(@Header("Authorization") String jwt, @Url String url, @Body ServerRequest request);

    @GET("helps")
    Call<ServerResponse> get_helps(@Query("lang_id") int lang_id, @Query("cat_id") int cat_id);

    @Headers("Accept: application/json")
    @GET()
    Call<ServerResponse> get_categories(@Url String url, @Query("lang_id") int lang_id);

    @Headers("Accept: application/json")
    @POST("guest/profile/change_notf_settings")
    Call<ServerResponse> change_notification_Settings(@Header("Authorization") String jwt, @Body ServerRequest request);

    @Headers("Accept: application/json")
    @GET("InitAndroidApp")
    Call<ServerResponse> InitAppFromServer();

    @GET("WelcomeSlides")
    Call<ServerResponse> getWelcomeSlides(@Query("lang_id") int lang_id);

    @GET("cafeRestaurants")
    Call<ServerResponse> getCafeRestaurantsList(@Header("Authorization") String jwt, @Query("lang_id") int lang_id);

    @Headers("Accept: application/json")
    @POST("getPrice")
    Call<Map<String, Service>> getServicesPrice(@Body ServerRequest request);


}
