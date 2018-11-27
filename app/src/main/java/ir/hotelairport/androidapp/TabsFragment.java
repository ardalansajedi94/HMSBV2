package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.hotelairport.androidapp.Models.Category;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ir.hotelairport.androidapp.GuestActivity.isRTL;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabsFragment extends Fragment {
    ViewPagerAdapter adapter;
    private int type;
    private PagerSlidingTabStrip tabLayout;
    private ViewPager viewPager;
    private SharedPreferences user_detail;
    private ProgressDialog progress;
    private Retrofit retrofit;
    private RequestInterface requestInterface;
    private Call<ServerResponse> response;
    private DatabaseHandler db;
    private int id;
    public TabsFragment() {
        // Required empty public constructor
    }

    public static TabsFragment newInstance(int type) {
        TabsFragment myFragment = new TabsFragment();

        Bundle args = new Bundle();
        args.putInt("type", type);
        myFragment.setArguments(args);

        return myFragment;
    }
    public static TabsFragment newInstance(int type,int id) {
        TabsFragment myFragment = new TabsFragment();

        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("id", id);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        db=new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        this.type = getArguments().getInt("type");
        if (getArguments().containsKey("id"))
            this.id=getArguments().getInt("id");
        else
            this.id=0;
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress = new ProgressDialog(getActivity());
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestInterface = retrofit.create(RequestInterface.class);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        if (isRTL(Locale.getDefault()))
        {
            tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            tabLayout.getTabsContainer().setGravity(Gravity.RIGHT);
        }
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        setupViewPager();
        tabLayout.setViewPager(viewPager);
        tabLayout.setIndicatorColor(getResources().getColor(R.color.white));
        viewPager.setOffscreenPageLimit(4);
        return view;
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        progress.show();
        switch (type) {
            case 1:
            case 2:
            case 3:
                response = requestInterface.get_categories("categories/" + String.valueOf(type), user_detail.getInt(Constants.LANGUAGE_ID, 1));
                RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                        progress.dismiss();
                        ServerResponse resp = response.body();
                        switch (response.code()) {
                            case 200:
                                if (resp != null) {
                                    ArrayList<Category> categories = resp.getCategories();
                                    if (categories.size() > 0) {
                                        if (isRTL(Locale.getDefault()))
                                        {
                                            switch (type) {
                                                case 1:
                                                    for (int i = categories.size()-1; i >=0; i--) {
                                                        adapter.addFragment(AboutCityFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                                case 2:
                                                    for (int i = categories.size()-1; i >=0; i--) {
                                                        adapter.addFragment(AboutHotelFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                                case 3:
                                                    for (int i = categories.size()-1; i >=0; i--) {
                                                        adapter.addFragment(HelpFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                            }
                                        }
                                        else
                                        {
                                            switch (type) {
                                                case 1:
                                                    for (int i = 0; i < categories.size(); i++) {
                                                        adapter.addFragment(AboutCityFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                                case 2:
                                                    for (int i = 0; i < categories.size(); i++) {
                                                        adapter.addFragment(AboutHotelFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                                case 3:
                                                    for (int i = 0; i < categories.size(); i++) {
                                                        adapter.addFragment(HelpFragment.newInstance(categories.get(i).getId()), categories.get(i).getName());
                                                    }
                                                    break;
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                        if (isRTL(Locale.getDefault()))
                                            viewPager.setCurrentItem(adapter.getCount());
                                    }

                                }
                                break;
                            default:
                                if (resp != null) {
                                    Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                                break;
                        }


                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        progress.dismiss();
                        Log.d("error:", t.getMessage());
                    }
                });
                break;
            case 4:
                response = requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT,""),"restaurants/"+String.valueOf(id)+"/services"+"?lang_id="+String.valueOf(user_detail.getInt(Constants.LANGUAGE_ID,1)));
                RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                        progress.dismiss();
                        ServerResponse resp = response.body();
                        switch (response.code()) {
                            case 200:
                                if (resp != null) {
                                    ArrayList<Category> categories = resp.getCategories();
                                    if (categories.size() > 0) {
                                        if (isRTL(Locale.getDefault()))
                                        {
                                            for (int i = categories.size()-1; i >=0; i--) {
                                                adapter.addFragment(MenuFragment.newInstance(type,id,categories.get(i).getId()), categories.get(i).getName());
                                            }
                                        }
                                        else
                                        {
                                            for (int i = 0; i < categories.size(); i++) {
                                                adapter.addFragment(MenuFragment.newInstance(type,id,categories.get(i).getId()), categories.get(i).getName());
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                        if (isRTL(Locale.getDefault()))
                                            viewPager.setCurrentItem(adapter.getCount());
                                    }

                                }
                                break;
                            default:
                                if (resp != null) {
                                    Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        progress.dismiss();
                        Log.d("error:", t.getMessage());
                    }
                });

                break;
            case 5:
                response =requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT,""),"coffeeShops/"+String.valueOf(id)+"/services"+"?lang_id="+String.valueOf(user_detail.getInt(Constants.LANGUAGE_ID,1)));
                RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                        progress.dismiss();
                        ServerResponse resp = response.body();
                        switch (response.code()) {
                            case 200:
                                if (resp != null) {
                                    ArrayList<Category> categories = resp.getCategories();
                                    if (categories.size() > 0) {
                                        if (isRTL(Locale.getDefault()))
                                        {
                                            for (int i = categories.size()-1; i >=0; i--) {
                                                adapter.addFragment(MenuFragment.newInstance(type,id,categories.get(i).getId()), categories.get(i).getName());
                                            }
                                        }
                                        else
                                        {
                                            for (int i = 0; i < categories.size(); i++) {
                                                adapter.addFragment(MenuFragment.newInstance(type,id,categories.get(i).getId()), categories.get(i).getName());
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                        if (isRTL(Locale.getDefault()))
                                            viewPager.setCurrentItem(adapter.getCount());
                                    }

                                }
                                break;
                            default:
                                if (resp != null) {
                                    Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        progress.dismiss();
                        Log.d("error:", t.getMessage());
                    }
                });
                break;


        }
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
