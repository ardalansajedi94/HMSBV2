package ir.hotelairport.androidapp.airportHotels.Service;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.EventBus.PassServicesToPax;
import ir.hotelairport.androidapp.airportHotels.api.model.BookServiceReq;


public class ServicePaxFragment extends Fragment {
    private EditText fullName, natCode, phone, email;
    Button accept;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getServices(final PassServicesToPax services){

        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hideKeyboard(getActivity());
                if(isValidation()){
                    BookServiceReq serviceReq = new BookServiceReq();
                    serviceReq.setNatCode(natCode.getText().toString());
                    serviceReq.setFullName(fullName.getText().toString());
                    serviceReq.setMobile(phone.getText().toString());
                    serviceReq.setEmail(email.getText().toString());
                    serviceReq.setServices(services.getServiceList());
                    ServiceBookConfirmFragment serviceBookConfirmFragment = new ServiceBookConfirmFragment();
                    serviceBookConfirmFragment.newInstance(serviceReq , services.getCount());
                    getFragmentManager().beginTransaction().replace(R.id.content_frame , serviceBookConfirmFragment).commit();

                }
            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = inflater.inflate(R.layout.fragment_service_pax, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fullName = view.findViewById(R.id.edit_name);
        natCode  = view.findViewById(R.id.edit_nat_code);
        phone    = view.findViewById(R.id.edit_cell_num);
        email    = view.findViewById(R.id.edit_email);
        accept= view.findViewById(R.id.accept);

        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fullName.setBackgroundResource(R.drawable.editor_valid);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        natCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                natCode.setBackgroundResource(R.drawable.editor_valid);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone.setBackgroundResource(R.drawable.editor_valid);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setBackgroundResource(R.drawable.editor_valid);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public boolean isValidation() {


            if (fullName.getText().toString().equals("") ) {
                Toast.makeText(getActivity(), "نام نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                fullName.setBackgroundResource(R.drawable.editor);
                return false;
            }
                if (natCode.getText().toString().equals("") ) {
                    Toast.makeText(getActivity(), "کد ملی نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                    natCode.setBackgroundResource(R.drawable.editor);
                    return false;
                }
                if (!isValidNatId(natCode.getText().toString())) {
                    Toast.makeText(getActivity(), "کد ملی صحیح وارد نشده است", Toast.LENGTH_LONG).show();
                    natCode.setBackgroundResource(R.drawable.editor);
                    return false;
                }
        if (!mobileValidation(phone.getText().toString())){
            Toast.makeText(getActivity(), "موبایل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
            phone.setBackgroundResource(R.drawable.editor);
            return false;
        }
            if (!isValidEmail(email.getText().toString())) {
                Toast.makeText(getActivity(), "ایمیل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
                email.setBackgroundResource(R.drawable.editor);
                return false;
            }



        return true;}


    public static boolean textPersian(String s) {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <=0x06FF || c== 0xFB8A || c==0x067E || c==0x0686 || c==0x06AF)
                return true;
        }
        return false;}

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidNatId(String natId){
        if (natId.equals("0000000000"))
            return false;
        else if (natId.equals("1111111111"))
            return false;
        else if (natId.equals("2222222222"))
            return false;
        else if (natId.equals("3333333333"))
            return false;
        else if (natId.equals("4444444444"))
            return false;
        else if (natId.equals("5555555555"))
            return false;
        else if (natId.equals("6666666666"))
            return false;
        else if (natId.equals("7777777777"))
            return false;
        else if (natId.equals("8888888888"))
            return false;
        else if (natId.equals("9999999999"))
            return false;
        else if (natId.equals("0123456789"))
            return false;
        else if (natId.equals("9876543210"))
            return false;
        else if (natId.equals("9999999999"))
            return false;
        else if (natId.length()!=10){
            return false;

        }
        else
            return true;
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


    public void showTextViewsAsMandatory ( TextView... tvs )
    {


        for ( TextView tv : tvs )
        {
            String text = tv.getText ().toString ();

            tv.setText ( Html.fromHtml ( text + "<font color=\"#ff0000\">" + " * " + "</font>" ) );
        }
    }
}
