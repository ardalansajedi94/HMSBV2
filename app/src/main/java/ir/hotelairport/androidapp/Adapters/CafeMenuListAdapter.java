package ir.hotelairport.androidapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.CafeMenuItem;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 9/25/2017.
 */

public class CafeMenuListAdapter extends BaseAdapter {
    private ArrayList<CafeMenuItem> _data;
    private Context _c;
    private ImageLoader imageLoader;
    private ProgressDialog progress;
    private SharedPreferences user_detail;

    public CafeMenuListAdapter(ArrayList<CafeMenuItem> data, Context c) {
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
        final CafeMenuItem item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_item_menu, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.menu_title);
        ImageView image = (ImageView) v.findViewById(R.id.menu_image);
        TextView material_tv = (TextView) v.findViewById(R.id.menu_material_tv);

        if (item.getTitle() != null)
            title_tv.setText(item.getTitle());
        imageLoader = ImageLoader.getInstance();
        if (item.getMaterial() != null) {
            String material[] = item.getMaterial().split(":");
            material_tv.setText(TextUtils.join(",", material));
        }
        if (item.getImages() != null) {
            if (!item.getImages().isEmpty()) {
                imageLoader.displayImage(Constants.MEDIA_BASE_URL + item.getImages().get(0).getImage_source(), image);
            }
        }

        return v;
    }
}