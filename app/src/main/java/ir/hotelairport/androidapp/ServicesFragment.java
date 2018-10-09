package ir.hotelairport.androidapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Adapters.ServicesListAdapter;
import ir.hotelairport.androidapp.Models.BlogContent;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    private GridView servicesList;
    private ArrayList<BlogContent> servicesListContent;
    private ServicesListAdapter servicesListAdapter;
    private DatabaseHandler db;
    SharedPreferences user_detail;
    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"services"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        servicesList = (GridView)view.findViewById(R.id.services_list);
        servicesListContent = new ArrayList<>();
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"browse_restaurants"),R.drawable.restaurants));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"browse_cafes"),R.drawable.coffeshops));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"table_reserve"),R.drawable.table_reserve));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"minibar_charge"),R.drawable.minibar_charge));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"taxi"),R.drawable.request_taxi));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"house_keeping"),R.drawable.house_keeping));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"clothes"),R.drawable.clothes));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"wake_up"),R.drawable.wakeup));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"stay"),R.drawable.stay));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"problems"),R.drawable.problems));
        if (user_detail.getString(Constants.LANGUAGE_LOCALE,"fa").equals("fa")){
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"short_stay"),R.drawable.img_service_with_room));
        servicesListContent.add(new BlogContent(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"service_only"),R.drawable.img_service_only));}
        servicesListAdapter = new ServicesListAdapter(servicesListContent,getActivity());
        servicesList.setAdapter(servicesListAdapter);
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment ;
                Bundle bundle;

                switch (i)
                {
                    case 0:
                        fragment=new CafeRestaurantsFragment();
                        bundle = new Bundle();
                        bundle.putInt("type", 4); // 1 : restaurant,2:cafes
                        fragment.setArguments(bundle);
                        break;
                    case 1:
                        fragment=new CafeRestaurantsFragment();
                         bundle = new Bundle();
                        bundle.putInt("type", 5); // 1 : restaurant,2:cafes
                        fragment.setArguments(bundle);
                        break;
                    case 2:
                        fragment=new TableReserveFragment();
                        break;
                    case 3:
                        fragment=new MinibarChargeFragment();
                        break;
//                    case 4:
//                        fragment = new RequestTaxiFragment();
//                        break;
                    case 5:
                        fragment= new RequestHouseKeepingFragment();
                        break;
                    case 6:
                        fragment=new RequestClothesFragment();
                        break;
                    case 7:
                        fragment=new RequestWakeupServiceFragment();
                        break;
                    case 8:
                        fragment = new RequestStayFragment();
                        break;
                    case 9:
                        fragment = new RequestReportProblemsFragment();
                        break;
                    case 10:
                        fragment = new ShortStayFragment();
                        break;
                    case 11:
                        fragment = new ServiceOnlyFragment();
                        break;
                    default:
                        fragment = new ServicesFragment();
                        break;
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

}
