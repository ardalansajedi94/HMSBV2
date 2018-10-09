package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.TimeLine;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 9/23/2017.
 */

public class TimeLineListAdapter extends BaseAdapter {
    private ArrayList<TimeLine> _data;
    private Context _c;
    private ImageLoader imageLoader ;
    public TimeLineListAdapter(ArrayList<TimeLine> data, Context c) {
        _data = data;
        _c = c;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
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
        final TimeLine item = _data.get(position);

        if (item.is_response())
        {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_time_line_response, null);

        }
        else
        {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_time_line, null);
        }

        TextView content_tv = (TextView) v.findViewById(R.id.time_line_content);
        TextView date_tv = (TextView) v.findViewById(R.id.time_line_time);
        content_tv.setText(item.getContent());
        date_tv.setText(item.getDate());
        return v;
    }
}