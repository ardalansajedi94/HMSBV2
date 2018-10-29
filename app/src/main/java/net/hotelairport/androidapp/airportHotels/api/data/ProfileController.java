package net.hotelairport.androidapp.airportHotels.api.data;



import net.hotelairport.androidapp.airportHotels.api.model.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileController {
    private HotelApi.ShowProfile showProfile;

    public ProfileController(HotelApi.ShowProfile showProfile) {
        this.showProfile = showProfile;
    }


    public void start(String token){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://hotelairport.ir/api/v1/").addConverterFactory(GsonConverterFactory.create()).build();
        HotelApi hotelApi =  retrofit.create(HotelApi.class);
        Call<ProfileResponse> call = hotelApi.ShowProfile( "application/json" ,"application/json" , token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful())
                    showProfile.onResponse(response.body());
                else
                    showProfile.onError( response.message() );

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

                try {
                    showProfile.onFailure(t.getCause().getMessage());
                }
                catch (NullPointerException e){

                }

            }
        });
    }
}
