package net.hotelairport.androidapp.airportHotels.Service;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.List;

import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.ComeFromWebActivity;
import net.hotelairport.androidapp.airportHotels.MainActivity;
import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.airportHotels.adapters.ServiceConfirmReviewAdapter;
import net.hotelairport.androidapp.airportHotels.api.data.BookServiceController;
import net.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import net.hotelairport.androidapp.airportHotels.api.model.BookResponse;
import net.hotelairport.androidapp.airportHotels.api.model.BookServiceReq;
import net.hotelairport.androidapp.airportHotels.dialogFragments.RulesDialog;


public class ServiceBookConfirmFragment extends Fragment {

     BookServiceReq bookServiceReq;
     List<Integer> count;
     TextView fullName , natCode, phone , email,totalPrice , rules;
     Button accept;
     RecyclerView recyclerView;
     CheckBox checkBox;
     ProgressBar progressBar;
     boolean flag=false;

    private int total;
    private double totalDob;
    private String totalStr;
    public ServiceBookConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (flag){
            Intent intent = new Intent(getActivity() , ComeFromWebActivity.class );
            getActivity().startActivity( intent );
            getActivity().finish();
        }
    }


    public ServiceBookConfirmFragment newInstance(BookServiceReq bookServiceReq , List<Integer> count) {
        ServiceBookConfirmFragment fragment = new ServiceBookConfirmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        this.bookServiceReq = bookServiceReq;
        this.count = count;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_book_confirm, container, false);


        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        fullName.setText(bookServiceReq.getFullName());
        natCode.setText(bookServiceReq.getNatCode());
        phone.setText(bookServiceReq.getMobile());
        email.setText(bookServiceReq.getEmail());
        ServiceConfirmReviewAdapter serviceConfirmReviewAdapter = new ServiceConfirmReviewAdapter(bookServiceReq.getServices() , count);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(serviceConfirmReviewAdapter);
        SpannableString styledString
                = new SpannableString("اطلاعات بالا مورد تایید است و "
                + "قوانین و مقررات"          // index 30 - 45
                + " را مطالعه و قبول دارم.");

        styledString.setSpan(new ForegroundColorSpan(Color.GREEN), 30, 45, 0);

        final RulesDialog rulesDialog = new RulesDialog();

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                ((MainActivity) getActivity()).RulesFragmentEvent();
            }
        };

        styledString.setSpan(clickableSpan, 30, 45, 0);

        rules.setMovementMethod(LinkMovementMethod.getInstance());

        rules.setText(styledString);


         accept = view.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){

                    JsonObject req = new JsonObject();
                    req.addProperty("api_token" , MyPreferenceManager.getInstace(getActivity()).getToken());
                    req.addProperty("ip" , "0");
                    req.addProperty("refId" , MyPreferenceManager.getInstace(getActivity()).getRefId());
                    req.addProperty("checkIn" , MyPreferenceManager.getInstace(getActivity()).getCheckIn());
                    JsonObject passenger = new JsonObject();
                    passenger.addProperty("fullName" , bookServiceReq.getFullName());
                    passenger.addProperty("nationalCode" , bookServiceReq.getNatCode());
                    passenger.addProperty("mobile" , bookServiceReq.getMobile());
                    passenger.addProperty("email" , bookServiceReq.getEmail());
                    passenger.addProperty("ageGroup" , "1");
                    passenger.addProperty("docType" , "i");
                    req.add("passenger",passenger);
                    JsonArray services = new JsonArray();
                    for (int i = 0 ; i<bookServiceReq.getServices().size() ; i++){
                        JsonObject servobj = new JsonObject();
                        servobj.addProperty("serviceId" , String.valueOf(bookServiceReq.getServices().get(i).getService_id()));
                        servobj.addProperty("amount" , count.get(i));
                        services.add(servobj);
                    }
                    req.add("services" , services);
                    BookServiceController bookServiceController = new BookServiceController(callBack);
                    bookServiceController.start(req,MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
                    recyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getActivity() , "لطفا ابتدا قوانین را مطالعه کرده و اطلاعات فوق را تایید کنید", Toast.LENGTH_LONG).show();
                }
            }
        });

        for (int i =0 ; i<bookServiceReq.getServices().size() ; i++){
            totalDob = totalDob +(bookServiceReq.getServices().get(i).getPrice()*count.get(i));
        }
        total = (int) (totalDob);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(total);
        totalPrice.setText(PersianDigitConverter.PerisanNumber("مبلغ کل: "+yourFormattedString+ " ريال"));

    }



 private void findViews(View view){
     fullName = view.findViewById(R.id.full_name);
     natCode = view.findViewById(R.id.nat_code);
     phone = view.findViewById(R.id.phone);
     email = view.findViewById(R.id.email);
     totalPrice = view.findViewById(R.id.total_price);
     rules = view.findViewById(R.id.check_txt);
     recyclerView = view.findViewById(R.id.service_list);
     checkBox = view.findViewById(R.id.checkbox);
     progressBar = view.findViewById(R.id.progress);
 }

    HotelApi.BookServiceCallBack callBack = new HotelApi.BookServiceCallBack() {
        @Override
        public void onResponse(BookResponse url) {
            flag =true;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.getPaymentLink()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        }

        @Override
        public void onFailure(String cause) {
            Toast.makeText(getActivity() , "مشکلی رخ داده است کمی بعد تر دوباره امتحان کنید" , Toast.LENGTH_LONG).show();
        }
    };



}
