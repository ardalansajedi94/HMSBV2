package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Models.Service;
import ir.hotelairport.androidapp.R;

public class ServiceOnlyListAdapter extends BaseAdapter {
    private ArrayList<Service> _data;
    private Context _c;

    public ServiceOnlyListAdapter(ArrayList<Service> data, Context c) {
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
        final Service item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_service_only, null);
        }
        TextView title_tv = (TextView) v.findViewById(R.id.titleTv);
        TextView priceTv = v.findViewById(R.id.priceTv);
        final TextView totalPriceTv = v.findViewById(R.id.totalPriceTv);
        final TextView count_tv = v.findViewById(R.id.count_tv);
        TextView minus_tv = v.findViewById(R.id.minus_tv);
        TextView plus_tv = v.findViewById(R.id.plus_tv);
        minus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCount() > 0) {
                    item.setCount(item.getCount() - 1);
                    notifyDataSetChanged();
                    count_tv.setText(String.valueOf(item.getCount()));
                    totalPriceTv.setText(String.valueOf(item.getCount() * item.getPrice_with_discount()));
                }
            }
        });
        plus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setCount(item.getCount() + 1);
                notifyDataSetChanged();
                count_tv.setText(String.valueOf(item.getCount()));
                totalPriceTv.setText(String.valueOf(item.getCount() * item.getPrice_with_discount()));

            }
        });
        TextView priceWithoutDiscountTV = v.findViewById(R.id.priceWithoutDiscountTV);
        FrameLayout priceWithoutDiscount = v.findViewById(R.id.priceWithoutDiscount);
        title_tv.setText(item.getName());

        totalPriceTv.setText(String.valueOf(item.getCount() * item.getPrice_with_discount()));
        count_tv.setText(String.valueOf(item.getCount()));
        if (item.getPrice_with_discount() != item.getPrice()) {
            priceTv.setText(String.valueOf(item.getPrice_with_discount()));
            priceWithoutDiscountTV.setText(String.valueOf(item.getPrice()));
            priceWithoutDiscount.setVisibility(View.VISIBLE);
        } else {
            priceWithoutDiscount.setVisibility(View.GONE);
            priceTv.setText(String.valueOf(item.getPrice()));
        }
        return v;
    }
}
