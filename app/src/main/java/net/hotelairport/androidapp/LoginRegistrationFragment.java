package net.hotelairport.androidapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.hotelairport.androidapp.Adapters.LoginRegistrationViewPagerAdapter;
import net.hotelairport.androidapp.SQLiteDB.DatabaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginRegistrationFragment extends Fragment {

    private TabLayout tabLayout;
    DatabaseHandler db;
    SharedPreferences user_detail;
    FragmentManager fragmentManager;
    Fragment fragment;
    public LoginRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login_registration, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);


        tabLayout = (TabLayout) view.findViewById(R.id.login_register_tabs);

        tabLayout.addTab(tabLayout.newTab().setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"login")));
        tabLayout.addTab(tabLayout.newTab().setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"register")));
        fragmentManager = getChildFragmentManager();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int select = bundle.getInt("select", 1); // 0 for selecting register and 1 for selecting login
            switchTab(select);
            switch (select)
            {
                case 0:
                    fragment = new LoginFragment();
                    break;
                case 1:
                    fragment=new RegisterFragment();
                    break;
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.login_register_container, fragment);
            fragmentTransaction.commit();
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    Log.i("tab","0");
                    fragment = new LoginFragment();
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    Log.i("tab","1");
                    fragment=new RegisterFragment();
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.login_register_container, fragment);
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return  view;
    }
    public void switchTab(int tab)
    {
        tabLayout.getTabAt(tab).select();
    }
    private void setupViewPager(ViewPager viewPager) {
        LoginRegistrationViewPagerAdapter adapter = new LoginRegistrationViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new LoginFragment(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"login"));
        adapter.addFragment(new RegisterFragment(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"register"));
        viewPager.setAdapter(adapter);
    }

}
