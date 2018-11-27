package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import ir.hotelairport.androidapp.Models.BlogContent;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostViewFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout sliderLayout;
    private BlogContent content;
    private ProgressDialog progress;

    TextView title_tv, content_tv;
    int post_type = 0;
    private SharedPreferences user_detail;
    DatabaseHandler db;

    public PostViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_view, container, false);
        db = new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        title_tv = (TextView) view.findViewById(R.id.PostViewTitle);
        content_tv = (TextView) view.findViewById(R.id.PostViewContent);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int post_id = bundle.getInt("id", 0);
            post_type = bundle.getInt("type", 0); // 1 for about hotel content and 2 for about city content and 3 for news and 4 for helps
            getContent(post_id);
        }
        return view;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider ", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void getContent(int id) {
        Log.d("post id", String.valueOf(id));
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
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
        if (post_type == 1) {
            response = requestInterface.dynamic_url("information/" + String.valueOf(id));
        } else if (post_type == 2) {
            response = requestInterface.dynamic_url("city_information/" + String.valueOf(id));
        } else if (post_type == 3) {
            response = requestInterface.dynamic_url("news/" + String.valueOf(id));

        } else {
            response = requestInterface.dynamic_url("helps/" + String.valueOf(id));
        }
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
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
                            sliderLayout.addOnPageChangeListener(PostViewFragment.this);
                            if (post_type == 3)
                                content = resp.getThe_news();
                            else if (post_type == 4) {
                                content = resp.getHelp();
                            } else
                                content = resp.getInformation();
                            title_tv.setText(content.getTitle());
                            content_tv.setText(content.getContent());
                            if (content.getImages() != null) {
                                if (content.getImages().size() > 0) {
                                    if (content.getImages().size() == 1) {
                                        sliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
                                    }
                                    for (int i = 0; i < content.getImages().size(); i++) {
                                        DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                                        sliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                        sliderView.image(Constants.MEDIA_BASE_URL + content.getImages().get(i).getImage_source());
                                        sliderLayout.addSlider(sliderView);
                                    }
                                } else {
                                    sliderLayout.setVisibility(View.GONE);
                                }

                            }

                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:", t.getMessage());
            }
        });
    }

}
