package net.hotelairport.androidapp.airportHotels.DailyStay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.hotelairport.androidapp.R;

import net.hotelairport.androidapp.airportHotels.EventBus.NextResultBackEvent;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.airportHotels.adapters.DailyRoomPagerAdapter;
import net.hotelairport.androidapp.airportHotels.api.model.RoomList;
import net.hotelairport.androidapp.airportHotels.api.model.RoomReview;

public class DailyStayBookFragment extends Fragment {
    ViewPager viewPager;
    TextView titleStrip;
    Button next;
    int position;
    JsonArray room = new JsonArray();
    List<RoomReview> roomReviewList = new ArrayList<>();

    public static DailyStayBookFragment newInstance() {

        Bundle args = new Bundle();

        DailyStayBookFragment fragment = new DailyStayBookFragment();
        fragment.setArguments(args);


        return fragment;
    }


    @Subscribe
    public void getRoom(NextResultBackEvent event){
        int size=0;
        room.add(event.getRoom());
        roomReviewList.add(event.getRoomReview());
        size = MyPreferenceManager.getInstace( getActivity() ).getRoom().size();
        if (position<size-1){
            viewPager.setCurrentItem(position+1 , true);
            MyPreferenceManager.getInstace(getActivity()).putPosition(position+1);
            position++;
            if (position == 0){
                if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                    titleStrip.setText("اتاق اول - هتل ایبیس ");
                else
                    titleStrip.setText("اتاق اول - هتل نووتل ");
            } else  if (position == 1){
                if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                    titleStrip.setText("اتاق دوم - هتل ایبیس ");
                else
                    titleStrip.setText("اتاق دوم - هتل نووتل ");
            }else  if (position == 2){
                if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                    titleStrip.setText("اتاق سوم - هتل ایبیس ");
                else
                    titleStrip.setText("اتاق سوم - هتل نووتل ");
            }else  if (position == 3){
                if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                    titleStrip.setText("اتاق چهارم - هتل ایبیس ");
                else
                    titleStrip.setText("اتاق چهارم - هتل نووتل ");
            }
            hideKeyboard(Objects.requireNonNull(getActivity()));
        }
        else {
            DailyReserveFragment reserveFragment = new DailyReserveFragment();
            RoomList roomList = new RoomList();
            roomList.setRoomReviews(roomReviewList);
            reserveFragment.newInstance(roomList);
            getFragmentManager().beginTransaction().replace(R.id.content_frame, reserveFragment).commit();
            hideKeyboard(Objects.requireNonNull(getActivity()));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stay_book_daily, container , false);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        position = viewPager.getCurrentItem();
        MyPreferenceManager.getInstace(getActivity()).putPosition(position);
        final DailyRoomPagerAdapter roomPagerAdapter = new DailyRoomPagerAdapter(getActivity().getSupportFragmentManager() , getActivity());
        viewPager.setAdapter(roomPagerAdapter);
        if (position == 0){
            if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                titleStrip.setText("اتاق اول - هتل ایبیس ");
            else
                titleStrip.setText("اتاق اول - هتل نووتل ");
        } else  if (position == 1){
            if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                titleStrip.setText("اتاق دوم - هتل ایبیس ");
            else
                titleStrip.setText("اتاق دوم - هتل نووتل ");
        }else  if (position == 2){
            if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                titleStrip.setText("اتاق سوم - هتل ایبیس ");
            else
                titleStrip.setText("اتاق سوم - هتل نووتل ");
        }else  if (position == 3){
            if (MyPreferenceManager.getInstace(getActivity()).getRoom().get( position ).getHotel_title_en() .equals(  "Ibis" ))
                titleStrip.setText("اتاق چهارم - هتل ایبیس ");
            else
                titleStrip.setText("اتاق چهارم - هتل نووتل ");
        }
        next.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(  new JsonObject() );
            }
        } );


    }
    private void findViews(View view){
        viewPager = view.findViewById(R.id.room_pager);
        titleStrip = view.findViewById(R.id.pager_title_strip);
        next = view .findViewById( R.id.accept );

    }


}
