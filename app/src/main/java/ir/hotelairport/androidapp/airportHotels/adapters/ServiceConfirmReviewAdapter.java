package ir.hotelairport.androidapp.airportHotels.adapters;

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
import ir.hotelairport.androidapp.airportHotels.api.model.Service;

public class ServiceConfirmReviewAdapter extends RecyclerView.Adapter<ServiceConfirmReviewAdapter.ViewHolder>{

    List<Service> services;
    List<Integer> count;
    private String startAt , endAt ;
    private int  startHour , endHour , startMinute , endMinute;
    public ServiceConfirmReviewAdapter(List<Service> services , List<Integer> count) {
        this.services = services;
        this.count = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_re_view , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (services.get(position).getTitle_fa().equals("صبحانه")&&services.get(position).getHotel_id()==1){
            holder.name.setText("صبحانه رستوران اوپن");
        }else if(services.get(position).getTitle_fa().equals("صبحانه")&&services.get(position).getHotel_id()==2){
            holder.name.setText("صبحانه رستوران گلکسی");
        }
        else
            holder.name.setText(services.get(position).getTitle_fa());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int)services.get(position).getPrice());
        holder.price.setText( PersianDigitConverter.PerisanNumber(yourFormattedString+ "ريال"));
        startHour = services.get(position).getStart_access()/100;
        startMinute = services.get(position).getStart_access()%100;
        endHour = services.get(position).getEnd_access()/100;
        endMinute = services.get(position).getEnd_access()%100;

        if (startHour == 0){
            startAt = PersianDigitConverter.PerisanNumber("00")+":"+PersianDigitConverter.PerisanNumber(String.valueOf(startMinute));
            if (startMinute == 0){
                startAt = PersianDigitConverter.PerisanNumber("00")+":"+PersianDigitConverter.PerisanNumber("00");
            }
        }
        else if (startMinute == 0){
            startAt = PersianDigitConverter.PerisanNumber(String.valueOf(startHour))+":"+PersianDigitConverter.PerisanNumber("00");
        }else {
            startAt= PersianDigitConverter.PerisanNumber(String.valueOf(startHour))+":"+PersianDigitConverter.PerisanNumber(String.valueOf(startMinute));
        }
        if (endHour == 0){
            endAt = PersianDigitConverter.PerisanNumber("00")+":"+PersianDigitConverter.PerisanNumber(String.valueOf(endMinute));
            if (endMinute == 0){
                endAt = PersianDigitConverter.PerisanNumber("00")+":"+PersianDigitConverter.PerisanNumber("00");
            }
        }
        else if (endMinute == 0) {
            endAt = PersianDigitConverter.PerisanNumber(String.valueOf(endHour)) + ":"+PersianDigitConverter.PerisanNumber("00");
        }
        else{
            endAt= PersianDigitConverter.PerisanNumber(String.valueOf(endHour))+":"+PersianDigitConverter.PerisanNumber(String.valueOf(endMinute));
        }
        if (services.get(position).getService_id()==233)
            holder.time.setText(PersianDigitConverter.PerisanNumber( "24" ) + " ساعته");
        else if (services.get(position).getService_id()==231)
            holder.time.setText(PersianDigitConverter.PerisanNumber( "24" ) + " ساعته");
        else if (services.get(position).getService_id()==232)
            holder.time.setText(PersianDigitConverter.PerisanNumber( "24" ) + " ساعته");
        else
        holder.time.setText(startAt+  " الی "+ endAt);
        holder.count.setText(PersianDigitConverter.PerisanNumber( String.valueOf(count.get(position) )));



    }
    @Override
    public int getItemCount() {
        return services.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView count, time,name, price;
        ViewHolder(View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count);
            time = itemView.findViewById( R.id.time_table);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
        }
    }



}
