package ir.hotelairport.androidapp.airportHotels.ShortStay;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.ComeFromWebActivity;
import ir.hotelairport.androidapp.airportHotels.MainActivity;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.adapters.PassengerDetailAdapter;
import ir.hotelairport.androidapp.airportHotels.api.data.BookRoomController;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.model.BookResponse;
import ir.hotelairport.androidapp.airportHotels.api.model.PaxReview;
import ir.hotelairport.androidapp.airportHotels.api.model.RoomList;
import ir.hotelairport.androidapp.airportHotels.api.model.RoomReview;
import ir.hotelairport.androidapp.airportHotels.dialogFragments.RulesDialog;


public class ReserveFragment extends android.support.v4.app.Fragment {
    RecyclerView recyclerView;
    TextView rules;
    Button next;
    RoomList roomList;
    ConstraintLayout main;
    ProgressBar progress;
    CheckBox checkBox;
    boolean flag = false;

    public ReserveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (flag) {
            Intent intent = new Intent(getActivity(), ComeFromWebActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public ReserveFragment newInstance(RoomList roomList) {
        this.roomList = roomList;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reserve, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.passenger);
        next = view.findViewById(R.id.accept);
        main = view.findViewById(R.id.main);
        rules = view.findViewById(R.id.check_txt);
        progress = view.findViewById(R.id.progress);
        checkBox = view.findViewById(R.id.checkbox);
        List<List<PaxReview>> paxReviewList = new ArrayList<>();
        List<PaxReview> paxReview = new ArrayList<>();
        List<RoomReview> roomReviewList = new ArrayList<>();
        roomReviewList = roomList.getRoomReviews();

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
        for (int i = 0; i < roomReviewList.size(); i++) {
            paxReviewList.add(roomReviewList.get(i).getPaxReviews());
        }
        String[][] poistion = new String[MyPreferenceManager.getInstace(getActivity()).getRoom().size()][2];
        for (int i = 0; i < paxReviewList.size(); i++) {
            for (int j = 0; j < paxReviewList.get(i).size(); j++) {
                paxReview.add(paxReviewList.get(i).get(j));
                if (i == 0 && j == 0)
                    poistion[i][j] = "اتاق اول مسافر اول";
                else if (i == 0 && j == 1)
                    poistion[i][j] = "اتاق اول مسافر دوم";
                else if (i == 1 && j == 0)
                    poistion[i][j] = "اتاق دوم مسافر اول";
                else if (i == 1 && j == 1)
                    poistion[i][j] = "اتاق دوم مسافر دوم";
                else if (i == 2 && j == 0)
                    poistion[i][j] = "اتاق سوم مسافر اول";
                else if (i == 2 && j == 1)
                    poistion[i][j] = "اتاق سوم مسافر دوم";
                else if (i == 3 && j == 0)
                    poistion[i][j] = "اتاق چهارم مسافر اول";
                else if (i == 3 && j == 1)
                    poistion[i][j] = "اتاق چهارم مسافر دوم";
            }

        }
        final PassengerDetailAdapter passengerDetailAdapter = new PassengerDetailAdapter(getActivity(), paxReview, poistion);
        recyclerView.setAdapter(passengerDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    JsonObject req = new JsonObject();
                    req.addProperty("api_token", MyPreferenceManager.getInstace(getActivity()).getToken());
                    req.addProperty("ip", "0");
                    req.addProperty("refId", MyPreferenceManager.getInstace(getActivity()).getRefId());
                    req.addProperty("reservedId", MyPreferenceManager.getInstace(getActivity()).getReservedId());
                    JsonArray room = new JsonArray();
                    for (int i = 0; i < roomList.getRoomReviews().size(); i++) {
                        JsonObject roomObj = new JsonObject();
                        roomObj.addProperty("roomId", roomList.getRoomReviews().get(i).getRoomId());
                        roomObj.addProperty("adults", roomList.getRoomReviews().get(i).getAdults());
                        roomObj.addProperty("childs", roomList.getRoomReviews().get(i).getChilds());
                        JsonArray pax = new JsonArray();
                        for (int j = 0; j < roomList.getRoomReviews().get(i).getPaxReviews().size(); j++) {
                            JsonObject paxObj = new JsonObject();
                            paxObj.addProperty("fullName", roomList.getRoomReviews().get(i).getPaxReviews().get(j).getFullName());
                            paxObj.addProperty("nationalCode", roomList.getRoomReviews().get(i).getPaxReviews().get(j).getNationalCode());
                            paxObj.addProperty("email", roomList.getRoomReviews().get(i).getPaxReviews().get(j).getEmail());
                            paxObj.addProperty("mobile", roomList.getRoomReviews().get(i).getPaxReviews().get(j).getMobile());
                            paxObj.addProperty("ageGroup", 1);
                            if (roomList.getRoomReviews().get(i).getPaxReviews().get(j).isDocType())
                                paxObj.addProperty("docType", "i");
                            else
                                paxObj.addProperty("docType", "p");
                            JsonArray services = new JsonArray();
                            for (int k = 0; k < roomList.getRoomReviews().get(i).getPaxReviews().get(j).getServiceReviews().size(); k++) {
                                JsonObject servicesObj = new JsonObject();
                                servicesObj.addProperty("serviceId", roomList.getRoomReviews().get(i).getPaxReviews().get(j).getServiceReviews().get(k).getService().getService_id());
                                servicesObj.addProperty("count", 1);
                                services.add(servicesObj);
                            }
                            paxObj.add("services", services);
                            pax.add(paxObj);
                        }
                        roomObj.add("pax", pax);
                        room.add(roomObj);
                    }
                    req.add("room", room);

                    BookRoomController bookRoomController = new BookRoomController(callBack);
                    bookRoomController.start(req, MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() + " " + MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
                    progress.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getActivity(), "لطفا ابتدا قوانین را مطالعه کرده و اطلاعات فوق را تایید کنید", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    HotelApi.BookRoomCallBack callBack = new HotelApi.BookRoomCallBack() {
        @Override
        public void onResponse(BookResponse url) {
            flag = true;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.getPaymentLink()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(browserIntent);

        }

        @Override
        public void onFailure(String cause) {
            main.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "مشکلی رخ داده است کمی بعد تر دوباره امتحان کنید", Toast.LENGTH_LONG).show();
        }
    };


}
