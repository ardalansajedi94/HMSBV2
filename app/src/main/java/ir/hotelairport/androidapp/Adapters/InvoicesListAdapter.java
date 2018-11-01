package ir.hotelairport.androidapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.Models.Invoice;
import ir.hotelairport.androidapp.R;

/**
 * Created by Mohammad on 10/3/2017.
 */

public class InvoicesListAdapter extends BaseAdapter {
    private ArrayList<Invoice> _data;
    private Context _c;
    private ImageLoader imageLoader ;
    public InvoicesListAdapter(ArrayList<Invoice> data, Context c) {
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
        final Invoice item = _data.get(position);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item_invoice, null);
        }
        TextView index_no_tv = (TextView) v.findViewById(R.id.no);
        TextView date_tv = (TextView) v.findViewById(R.id.date);
        TextView file_tv = (TextView) v.findViewById(R.id.file);
        TextView amount_tv = (TextView) v.findViewById(R.id.amount);
        TextView process_tv = (TextView) v.findViewById(R.id.process);
        if (position==0)
        {
            index_no_tv.setText("#");
            date_tv.setText(_c.getResources().getString(R.string.date));
            file_tv.setText(_c.getResources().getString(R.string.file));
            amount_tv.setText(_c.getResources().getString(R.string.amount));
            process_tv.setText(_c.getResources().getString(R.string.processes));
            date_tv.setTextColor(_c.getResources().getColor(R.color.colorPrimary));
            amount_tv.setTextColor(_c.getResources().getColor(R.color.colorPrimary));
            process_tv.setTextColor(_c.getResources().getColor(R.color.colorPrimary));
            index_no_tv.setTextColor(_c.getResources().getColor(R.color.colorPrimary));
            process_tv.setBackgroundColor(Color.WHITE);
            process_tv.setBackground(_c.getResources().getDrawable(R.drawable.textview_border));
            file_tv.setBackgroundColor(Color.WHITE);
            file_tv.setBackground(_c.getResources().getDrawable(R.drawable.textview_border));
            file_tv.setTextColor(_c.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            index_no_tv.setText(String.valueOf(position));
            date_tv.setText(item.getCreated_at());
            date_tv.setTextColor(_c.getResources().getColor(R.color.black));
            amount_tv.setTextColor(_c.getResources().getColor(R.color.black));
            index_no_tv.setTextColor(_c.getResources().getColor(R.color.black));
         if (item.getHas_response()==1)
         {
             amount_tv.setText(item.getResponse());
             process_tv.setText(_c.getResources().getString(R.string.pay_now));
             process_tv.setTextColor(Color.WHITE);
             process_tv.setBackgroundColor(_c.getResources().getColor(R.color.colorPrimary));
             process_tv.setClickable(true);
             process_tv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Toast.makeText(_c,"paying steps",Toast.LENGTH_SHORT).show();
                 }
             });
         }
         else
         {
             amount_tv.setText("---");
             process_tv.setText(_c.getResources().getString(R.string.processing));
             process_tv.setTextColor(Color.BLACK);
             process_tv.setBackgroundColor(Color.WHITE);
             process_tv.setClickable(false);
             process_tv.setBackground(_c.getResources().getDrawable(R.drawable.textview_border));

         }
         if (item.getFile_source()!=null)
         {
             file_tv.setText(_c.getResources().getString(R.string.download));
             file_tv.setTextColor(Color.WHITE);
             file_tv.setBackgroundColor(_c.getResources().getColor(R.color.colorPrimary));
             file_tv.setClickable(true);
             file_tv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Uri intentUri = Uri.parse(Constants.MEDIA_BASE_URL + item.getFile_source());
                     Intent intent = new Intent();
                     intent.setAction(Intent.ACTION_VIEW);
                     intent.setData(intentUri);
                     _c.startActivity(intent);
                 }
             });
         }
         else
         {
             file_tv.setText("---");
             file_tv.setClickable(false);
             file_tv.setTextColor(Color.BLACK);
             file_tv.setBackgroundColor(Color.WHITE);
             file_tv.setBackground(_c.getResources().getDrawable(R.drawable.textview_border));
         }

        }
        return v;
    }
}