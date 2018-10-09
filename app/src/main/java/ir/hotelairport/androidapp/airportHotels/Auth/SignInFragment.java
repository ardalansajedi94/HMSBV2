package ir.hotelairport.androidapp.airportHotels.Auth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import ir.hotelairport.androidapp.AuthActivity;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.MainActivity;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.LoginController;
import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;

public class SignInFragment extends Fragment {
    EditText email , password;
    Button signUp , login;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_signin , container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        email = view.findViewById( R.id.email );
        password = view.findViewById( R.id.password );
        signUp = view.findViewById( R.id.sign_up );
        login = view.findViewById( R.id.submit );

        signUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();
                getFragmentManager().beginTransaction().add( R.id.content_frame , signUpFragment ).addToBackStack( null ).commit();
            }
        } );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail( email.getText().toString() )){
                    JsonObject req = new JsonObject();
                    req.addProperty( "grant_type" , "password");
                    req.addProperty( "client_id" , 2 );
                    req.addProperty( "client_secret" , "HBJA1ju6oUhqO65qGz32qyK8bu9Kzx76p3zILsEH" );
                    req.addProperty( "scope" , "" );
                    req.addProperty( "username" , email.getText().toString());
                    req.addProperty( "password" , password.getText().toString() );
                    LoginController loginController = new LoginController( callBack );
                    loginController.start( req );
                }
                else {
                    Toast.makeText( getActivity() , "ایمیل نا معتبر است" , Toast.LENGTH_LONG).show();
                }
            }
        } );
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    HotelApi.LoginCallBack callBack = new HotelApi.LoginCallBack() {
        @Override
        public void onResponse(LoginRes response) {
            MyPreferenceManager.getInstace( getActivity() ).putLoginRes( response );
            Intent myIntent = new Intent(getActivity() , MainActivity.class);
            ((AuthActivity)getActivity()).startActivity(myIntent);
            getActivity().finish( );
        }

        @Override
        public void onFailure(String cause) {
            Toast.makeText( getActivity() , "مشکلی پیش آمده است لطفا از اتصال اینترنت خود مطمئن شوید" , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(String cause) {
            Toast.makeText( getActivity() , "مشکلی در ورود شما پیش امده است لطفا از درست بودن رمز عبور و پست الکترونیکی خود مطمئن شوید" , Toast.LENGTH_LONG).show();
        }
    };
}
