package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.WelcomeTabsItem;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 8/24/2017.
 */

public class WelcomeViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ImageLoader imageLoader;
    private List<WelcomeTabsItem> WelcomeItemsList;
    private LayoutInflater layoutInflater;

    public WelcomeViewPagerAdapter(Context context, List<WelcomeTabsItem> WelcomeItemsList) {
        this.context = context;
        this.WelcomeItemsList = WelcomeItemsList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return WelcomeItemsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.guest_user_main_screen, container, false);
        WelcomeTabsItem WelcomeItem = WelcomeItemsList.get(position);
        ImageView WelcomeImage = (ImageView) view.findViewById(R.id.welcome_item_image);
        TextView WelcomeTitle = (TextView) view.findViewById(R.id.welcome_item_title);
        TextView WelcomeDescription = (TextView) view.findViewById(R.id.welcome_item_description);
        WelcomeDescription.setMovementMethod(new ScrollingMovementMethod());
        //bind value to the View Widgets
        Log.i("title", WelcomeItem.getTitle());
        WelcomeTitle.setText(WelcomeItem.getTitle());
        WelcomeDescription.setText(WelcomeItem.getContent());
        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(Constants.MEDIA_BASE_URL + WelcomeItem.getImage_source(), WelcomeImage);
        container.addView(view);

        return view;
    }
}
