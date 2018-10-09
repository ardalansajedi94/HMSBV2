package ir.hotelairport.androidapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.api.model.Price;
import ir.hotelairport.androidapp.airportHotels.api.model.Service;

public class MonthPriceAdapter extends RecyclerView.Adapter<MonthPriceAdapter.ViewHolder>{

    List<Price> prices;
    public MonthPriceAdapter(List<Price> prices) {
        this.prices = prices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_price_adapter , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(prices.get( position ).getPriceByMonth() );
        holder.price.setText( PersianDigitConverter.PerisanNumber( String.valueOf( yourFormattedString ) +" ریال"));
        holder.month.setText( PersianDigitConverter.PerisanNumber( prices.get( position ).getMonth() ) );

    }
    @Override
    public int getItemCount() {
        return prices.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView month, price;
        ViewHolder(View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.price);
            month = itemView.findViewById(R.id.month);
        }
    }



}
