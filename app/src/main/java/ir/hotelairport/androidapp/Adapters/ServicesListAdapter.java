package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Models.BlogContent;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 9/2/2017.
 */

public class ServicesListAdapter extends BaseAdapter {
    private ArrayList<BlogContent> _data;
    private Context _c;

    public ServicesListAdapter(ArrayList<BlogContent> data, Context c) {
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
        final BlogContent item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_services, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.service_title);
        ImageView image = (ImageView) v.findViewById(R.id.service_image);
        title_tv.setText(item.getTitle());
        image.setImageResource(item.getInternalImage());
        return v;
    }
}
