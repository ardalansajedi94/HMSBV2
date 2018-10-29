package net.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import net.hotelairport.androidapp.airportHotels.PersianDate;
import net.hotelairport.androidapp.Constants;
import net.hotelairport.androidapp.Models.BlogContent;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;

import saman.zamani.persiandate.PersianDateFormat;

/**
 * Created by Mohammad on 9/1/2017.
 */

public class AboutCityListAdapter extends BaseAdapter {
    private ArrayList<BlogContent> _data;
    private Context _c;
    private ImageLoader imageLoader ;
    public AboutCityListAdapter(ArrayList<BlogContent> data, Context c) {
        _data = data;
        Collections.reverse( _data );

        _c = c;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return _data.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return _data.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        final BlogContent item = _data.get(position);

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_about_city, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.about_city_card_view_title);
        TextView description_tv = (TextView) v.findViewById(R.id.about_city_card_view_description);
        ImageView image = (ImageView) v.findViewById(R.id.about_city_card_view_image);
        TextView date_tv = (TextView)v.findViewById(R.id.content_date);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = fmt.parse(item.getCreated_at());
            PersianDateFormat pdformater = new PersianDateFormat();
            PersianDate now=new PersianDate();
            saman.zamani.persiandate.PersianDate jdate;
            jdate =pdformater.parseGrg(item.getCreated_at(),"yyyy-MM-dd HH:mm:ss");
            date_tv.setText(PersianDigitConverter.PerisanNumber( jdate.getShYear() + "/" + jdate.getShMonth() + "/" + jdate.getShDay() ));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        title_tv.setText(item.getTitle());
        description_tv.setText(item.getContent());
        imageLoader= ImageLoader.getInstance();
        if (item.getImages()!=null)
        {
            Log.d("images are there","yes");
            if (item.getImages().size()>0)
            {
                imageLoader.displayImage(Constants.MEDIA_BASE_URL+item.getImages().get(0).getImage_source(),image);

            }
            else
            {
                image.setImageResource(R.drawable.fuch_this_shit);
            }

        }
        else
            image.setImageResource(R.drawable.fuch_this_shit);
        return v;
    }

}
