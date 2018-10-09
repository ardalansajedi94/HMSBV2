package ir.hotelairport.androidapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.Constants;
import ir.hotelairport.androidapp.LoggedInActivity;
import ir.hotelairport.androidapp.Models.Basket;
import ir.hotelairport.androidapp.Models.CafeRestaurant;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.RetrofitWithRetry;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerRequest;
import ir.hotelairport.androidapp.Server.ServerResponse;
import ir.hotelairport.androidapp.foodBasketFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mohammad on 9/25/2017.
 */

public class FoodBasketListAdapter extends BaseAdapter {
    private ArrayList<Basket> _data;
    private Context _c;
    private SharedPreferences user_detail;
    private ProgressDialog progress;
    private Retrofit retrofit;
    private RequestInterface requestInterface;
    private DatabaseHandler db;
    private Call<ServerResponse> response;
    TextView plus_tv,minus_tv,count_tv;
    public FoodBasketListAdapter(ArrayList<Basket> data, Context c) {
        _data = data;
        _c = c;
        db=new DatabaseHandler(_c);
        progress = new ProgressDialog(_c);
        progress.setMessage(_c.getResources().getString(R.string.connecting_to_server));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestInterface = retrofit.create(RequestInterface.class);
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
        final Basket item = _data.get(position);
        user_detail=_c.getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.new_list_item_basket, null);
        }
        count_tv=(TextView)v.findViewById(R.id.count_tv);
        minus_tv=(TextView)v.findViewById(R.id.minus_tv);
        plus_tv=(TextView)v.findViewById(R.id.plus_tv);
        TextView menu_title= (TextView)v.findViewById(R.id.menu_title);
        menu_title.setText(item.getTitle());
        count_tv.setText(String.valueOf(item.getCount()));
        minus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCount()>1)
                {
                    _data.get(position).setCount(_data.get(position).getCount()-1);
                    edit_item(position,item.getCount()-1);
                }
                else
                {
                     AlertDialog.Builder builder = new AlertDialog.Builder(_c);
                    builder.setMessage(_c.getString(R.string.delete_basket_item_confirm)).setPositiveButton(_c.getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (_data.size()==1)
                            {
                                remove_item(position,true);
                            }
                            else
                            {
                                remove_item(position,false);
                            }
                        }
                    })
                            .setNegativeButton(_c.getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).show();


                }
            }
        });
        plus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _data.get(position).setCount(_data.get(position).getCount()+1);
                edit_item(position,item.getCount()+1);
            }
        });

        return v;
    }
    private void edit_item(int position,int count)
    {
        progress.show();
        ServerRequest request=new ServerRequest();
        request.setCount(count);
        request.set_method("PUT");
        response = requestInterface.dynamic_post_request(user_detail.getString(Constants.JWT,""),"guest/basket/"+String.valueOf(_data.get(position).getId()),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(_c,_c.getResources().getString(R.string.order_updated),Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(_c, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(_c, db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Log.d("error:",t.getMessage());
            }
        });
    }
    private void remove_item(final int position, final boolean last_item)
    {


        progress.show();
        ServerRequest request=new ServerRequest();
        request.set_method("DELETE");
        response = requestInterface.dynamic_post_request(user_detail.getString(Constants.JWT,""),"guest/basket/"+String.valueOf(_data.get(position).getId()),request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            _data.remove(position);
                            notifyDataSetChanged();
                            if (last_item)
                            {
                                FragmentManager fm = ((LoggedInActivity)_c).getSupportFragmentManager();
                                foodBasketFragment current_fragment = (foodBasketFragment)fm.findFragmentByTag("CURRENT_FRAGMENT");
                                current_fragment.getBasket(false);
                            }
                            Toast.makeText(_c,_c.getResources().getString(R.string.order_updated),Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(_c, resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(_c, db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Log.d("error:",t.getMessage());
            }
        });
    }
}