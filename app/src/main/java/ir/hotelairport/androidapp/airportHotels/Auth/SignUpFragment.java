package ir.hotelairport.androidapp.airportHotels.Auth;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.hotelairport.androidapp.AuthActivity;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.MainActivity;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.RegisterController;
import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;

public class SignUpFragment extends Fragment {
    EditText name , family , mobile , email , password, confirmPassword;
    Button submit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_signup , container , false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        name= view.findViewById( R.id.name );
        family= view.findViewById( R.id.family );
        mobile= view.findViewById( R.id.mobile );
        password= view.findViewById( R.id.password );
        email= view.findViewById( R.id.email );
        confirmPassword= view.findViewById( R.id.password_confirm );
        submit= view.findViewById( R.id.submit );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(getActivity());
                if(isValidation()){
                    JsonObject req = new JsonObject();
                    req.addProperty( "firstName" , name.getText().toString() );
                    req.addProperty( "lastName" , family.getText().toString() );
                    req.addProperty( "email" , email.getText().toString() );
                    req.addProperty( "mobile_number" , mobile.getText().toString() );
                    req.addProperty( "password" , password.getText().toString() );
                    req.addProperty( "password_confirmation" , confirmPassword.getText().toString() );
                    RegisterController registerController = new RegisterController( callBack );
                    registerController.start( req );

                }

            }
        } );

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }
    public void showTextViewsAsMandatory ( TextView... tvs )
    {


        for ( TextView tv : tvs )
        {
            String text = tv.getText ().toString ();

            tv.setText ( Html.fromHtml ( text + "<font color=\"#ff0000\">" + " * " + "</font>" ) );
        }
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean mobileValidation(String mobile){

        Pattern pattern = Pattern.compile("^[0][9][1][0-9]{8,8}$");
        Pattern pattern1 = Pattern.compile("^[0][9][0][0-9]{8,8}$");
        Pattern pattern2 = Pattern.compile("^[0][9][2][0-9]{8,8}$");
        Pattern pattern3 = Pattern.compile("^[0][9][3][0-9]{8,8}$");
        Pattern pattern4 = Pattern.compile("^[0][9][4][0-9]{8,8}$");
        Matcher matcher = pattern.matcher(mobile);
        Matcher matcher1 = pattern1.matcher(mobile);
        Matcher matcher2 = pattern2.matcher(mobile);
        Matcher matcher3 = pattern3.matcher(mobile);
        Matcher matcher4 = pattern4.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }
        else if (matcher1.matches()){
            return true;
        }
        else if (matcher2.matches()){
            return true;
        }
        else if (matcher3.matches()){
            return true;
        }
        else if (matcher4.matches()){
            return true;
        }
        else
        {
            return false;
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static boolean textPersian(String s) {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <=0x06FF || c== 0xFB8A || c==0x067E || c==0x0686 || c==0x06AF)
                return true;
        }
        return false;}

    public boolean isValidation() {


        if (name.getText().toString().equals("") ) {
            Toast.makeText(getActivity(), "نام نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
            return false;
        }
        if (family.getText().toString().equals("") ) {
            Toast.makeText(getActivity(), "نام خانوادگی نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
            return false;
        }


        if (!isValidEmail(email.getText().toString())) {
            Toast.makeText(getActivity(), "ایمیل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!mobileValidation(mobile.getText().toString())){
            Toast.makeText(getActivity(), "موبایل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.getText().toString().equals("") ) {
            Toast.makeText(getActivity(), "گذرواژه نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
            return false;
        }
        if (confirmPassword.getText().toString().equals("") ) {
            Toast.makeText(getActivity(), "تاییدیه گذرواژه نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!confirmPassword.getText().toString().equals( password.getText().toString() )  ) {
            Toast.makeText(getActivity(), "تاییدیه گذرواژه با گذرواژه برابری نمی کند", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;}

        HotelApi.RegisterCallback callBack = new HotelApi.RegisterCallback() {
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
                Toast.makeText( getActivity() , "مشکلی در ثبت نام شما پیش امده است لطفا کمی بعد تر امتحان کنید" , Toast.LENGTH_LONG).show();
            }
        };
}
