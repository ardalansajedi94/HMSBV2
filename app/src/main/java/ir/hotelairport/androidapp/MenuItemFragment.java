package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.Models.CafeMenuItem;
import ir.hotelairport.androidapp.Models.RestaurantMenuItem;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerRequest;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuItemFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private SliderLayout sliderLayout;
    private CafeMenuItem cafeMenuItem;
    private RestaurantMenuItem restaurantMenuItem;
    private ProgressDialog progress;
    private TextView foodTitleTV, foodContentTV, foodMaterialTV,foodUnitTV,MaterialTitleTV;
    private Spinner foodCountSP;
    private int type; //1 for restaurant and 2 for cafe
    int id;
    private SharedPreferences user_detail;
    Button order_btn;
    DatabaseHandler db;
    public MenuItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu_item, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.order);
        db=new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        sliderLayout = (SliderLayout)view.findViewById(R.id.slider);
        foodTitleTV =(TextView)view.findViewById(R.id.FoodNameTV);
        foodContentTV =(TextView)view.findViewById(R.id.FoodContent);
        foodMaterialTV =(TextView)view.findViewById(R.id.FoodMaterial);
        foodUnitTV =(TextView)view.findViewById(R.id.food_unit);
        MaterialTitleTV =(TextView)view.findViewById(R.id.materialTitleTv);
        foodCountSP=(Spinner) view.findViewById(R.id.food_count);
        order_btn=(Button) view.findViewById(R.id.add_to_basket_btn);
        foodContentTV.setMovementMethod(new ScrollingMovementMethod());
        foodMaterialTV.setMovementMethod(new ScrollingMovementMethod());
        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.type = bundle.getInt("type",1);
            this.id = bundle.getInt("id",1);
        }
        List<Integer> counts = new ArrayList<Integer>();
        for (int i=1;i<=10;i++)
            counts.add(i);
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, counts);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodCountSP.setAdapter(dataAdapter);
        getItem();
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count_no;
                count_no=Integer.parseInt(foodCountSP.getSelectedItem().toString());
                switch (type)
                {
                    case 1:
                        add_to_basket(restaurantMenuItem.getId(),count_no,type);
                        break;
                    case 2:
                        add_to_basket(cafeMenuItem.getId(),count_no,type);
                        break;
                }
            }
        });
        MaterialTitleTV.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"material"));
        return view;
    }
    private void add_to_basket(int item_id,int count,int type)
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
        Call<ServerResponse> response;
        ServerRequest request=new ServerRequest();
        request.setFood_id(item_id);
        request.setType(type);
        request.setCount(count);
        response = requestInterface.order(user_detail.getString(Constants.JWT,""),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(),db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"order_success"),Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });
    }
    private void getItem()
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
        Call<ServerResponse> response;
        if (this.type ==1)
        {
            response = requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT,""),"restaurants/menu/"+String.valueOf(id));
        }
        else
        {
            response = requestInterface.dynamic_url_with_jwt(user_detail.getString(Constants.JWT,""),"coffeeShops/menu/"+String.valueOf(id));
        }

        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>(){
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.stopAutoCycle();
                            sliderLayout.addOnPageChangeListener(MenuItemFragment.this);
                            if (type==1)
                            {
                                restaurantMenuItem=resp.getRestaurant_item();
                                foodTitleTV.setText(restaurantMenuItem.getTitle());
                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                                {
                                    foodContentTV.setText(Html.fromHtml(restaurantMenuItem.getContent(), Html.FROM_HTML_MODE_COMPACT));
                                }

                                else
                                {
                                    foodContentTV.setText(Html.fromHtml(restaurantMenuItem.getContent()));
                                }
                                String material[]=restaurantMenuItem.getMaterial().split(":");
                                foodMaterialTV.setText(TextUtils.join(",",material));
                                foodUnitTV.setText(restaurantMenuItem.getUnit());
                                if (restaurantMenuItem.getImages()!=null)
                                {
                                    Log.i("images size",String.valueOf(restaurantMenuItem.getImages().size()));
                                    for (int i=0;i<restaurantMenuItem.getImages().size();i++)
                                    {
                                        DefaultSliderView sliderView= new DefaultSliderView(getActivity());
                                        sliderView.image(Constants.MEDIA_BASE_URL+restaurantMenuItem.getImages().get(i).getImage_source());
                                        sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                                        sliderLayout.addSlider(sliderView);
                                    }

                                    if (restaurantMenuItem.getImages().size()==1) {
                                        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                                    }
                                }
                            }
                            else
                            {
                                cafeMenuItem=resp.getCafe_item();
                                cafeMenuItem=resp.getCafe_item();
                                foodTitleTV.setText(cafeMenuItem.getTitle());
                                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                                {
                                    foodContentTV.setText(Html.fromHtml(cafeMenuItem.getContent(), Html.FROM_HTML_MODE_COMPACT));
                                }

                                else
                                {
                                    foodContentTV.setText(Html.fromHtml(cafeMenuItem.getContent()));

                                }
                                String material[]=cafeMenuItem.getMaterial().split(":");
                                foodMaterialTV.setText(TextUtils.join(",",material));
                                foodUnitTV.setText(cafeMenuItem.getUnit());
                                if (cafeMenuItem.getImages()!=null)
                                {
                                    for (int i=0;i<cafeMenuItem.getImages().size();i++)
                                    {
                                        DefaultSliderView sliderView= new DefaultSliderView(getActivity());
                                        sliderView.image(Constants.MEDIA_BASE_URL+cafeMenuItem.getImages().get(i).getImage_source());
                                        sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);
                                        sliderLayout.addSlider(sliderView);
                                    }
                                    Log.d("images size",String.valueOf(cafeMenuItem.getImages().size()));
                                    if (cafeMenuItem.getImages().size()==1)
                                        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                                }
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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });
    }
    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
       // Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider ", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

}
