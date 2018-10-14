package ir.hotelairport.androidapp.airportHotels;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.adapters.PerchesAdapter;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.PurchaseController;
import ir.hotelairport.androidapp.airportHotels.api.model.Voucher;

public class MyTicketFragment extends Fragment {

    RecyclerView perchesList;
    Button returnBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_ticket ,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        perchesList = view.findViewById(R.id.purch_list);
        returnBtn = view.findViewById(R.id.return_btn);
        PurchaseController purchaseController = new PurchaseController(callBack);
        purchaseController.start(MyPreferenceManager.getInstace(getActivity()).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(getActivity()).getLoginRes().getAccess_token());
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

    }
    HotelApi.PurchaseListCallBack callBack = new HotelApi.PurchaseListCallBack() {
        @Override
        public void onResponse(List<Voucher> voucherList) {
            Collections.reverse(voucherList);
            PerchesAdapter adapter = new PerchesAdapter(voucherList , getActivity());
            perchesList.setLayoutManager( new LinearLayoutManager(getActivity()));
            perchesList.setAdapter(adapter);
        }

        @Override
        public void onFailure(String cause) {

        }

        @Override
        public void onError(String cause) {

        }
    };
}
