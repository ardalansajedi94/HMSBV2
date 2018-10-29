package net.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import net.hotelairport.androidapp.Adapters.WelcomeViewPagerAdapter;
import net.hotelairport.androidapp.Models.WelcomeTabsItem;
import net.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import net.hotelairport.androidapp.Server.RequestInterface;
import net.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.hotelairport.androidapp.GuestActivity.isRTL;


/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeSlidesFragment extends Fragment {
    private ViewPager viewPager;
    private WelcomeViewPagerAdapter mAdapter;
    private Handler handler;
    private TextView login_tv,register_tv;
    private ImageView bg_logo;
    private final int delay = 5000;
    private int page = 0;
    ProgressDialog progress;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    FragmentManager fragmentManager;
    ArrayList<WelcomeTabsItem>slides=new ArrayList<>();
    Runnable runnable = new Runnable() {
        public void run() {
            if (isRTL(Locale.getDefault()))
            {
                if (page==0) {
                    page =mAdapter.getCount();
                } else {
                    page--;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
            else
            {
                if (mAdapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }

        }
    };

    public WelcomeSlidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_welcome_slides, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        handler = new Handler();
        viewPager = (ViewPager)view.findViewById(R.id.welcome_view_pager);
        mAdapter = new WelcomeViewPagerAdapter(getActivity(),slides);
        viewPager.setAdapter(mAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.welcome_tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        getWelcomeSlides();
        if (isRTL(Locale.getDefault()))
        {
            page=mAdapter.getCount();
            tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                page=position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentManager = getActivity().getSupportFragmentManager();
        login_tv =(TextView) view.findViewById(R.id.login_tv);
        register_tv= (TextView)view.findViewById(R.id.register_tv);
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginRegistrationFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("select", 0);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LoginRegistrationFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("select", 1);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(db.getHOtelInfoForLang(user_detail.getInt(Constants.LANGUAGE_ID,1)).getHotel_name());
        bg_logo=(ImageView)view.findViewById(R.id.bg_logo);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
         handler.postDelayed(runnable, delay);
    }
    @Override
    public void onPause() {
        super.onPause();
           handler.removeCallbacks(runnable);
    }
    private void getWelcomeSlides()
    {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response ;
        response = requestInterface.getWelcomeSlides(user_detail.getInt(Constants.LANGUAGE_ID,1));
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            slides = resp.getSlides();
                            mAdapter = new WelcomeViewPagerAdapter(getActivity(), slides);
                            mAdapter.notifyDataSetChanged();
                            viewPager.setAdapter(mAdapter);
                            if (slides.size() == 0)
                            {
                                bg_logo.setImageAlpha(255);
                            }
                            else
                            {
                                bg_logo.setVisibility(View.GONE);
                            }
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Log.d("error:",t.getMessage());
            }
        });
    }

}
