package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.BlogContent;
import ir.hotelairport.androidapp.Models.CafeRestaurant;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 9/25/2017.
 */

public class CafeRestaurantsListAdapter extends BaseAdapter {
    private ArrayList<CafeRestaurant> _data;
    private Context _c;
    private ImageLoader imageLoader ;
    public CafeRestaurantsListAdapter(ArrayList<CafeRestaurant> data, Context c) {
        _data = data;
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
        final CafeRestaurant item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_cafe_restaurant, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.cafe_restaurant_title);
        ImageView image = (ImageView) v.findViewById(R.id.cafe_restaurant_image);
        title_tv.setText(item.getName());
        imageLoader= ImageLoader.getInstance();
        if (item.getImages()!=null)
        {
            if (!item.getImages().isEmpty())
            {
                imageLoader.displayImage(Constants.MEDIA_BASE_URL+item.getImages().get(0).getImage_source(),image);
            }
            else
            {
                imageLoader.displayImage("http://portal.samplehotel.ir/assets/guests/images/restaurant.jpg",image);

            }
        }
        return v;
    }
}