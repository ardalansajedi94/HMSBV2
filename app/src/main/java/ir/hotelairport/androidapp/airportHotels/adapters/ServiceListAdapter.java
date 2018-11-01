package ir.hotelairport.androidapp.airportHotels.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.airportHotels.EventBus.PassServicesToFragment;
import ir.hotelairport.androidapp.airportHotels.EventBus.ReserveButtonClickedServices;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.api.model.Service;



public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder>{
    List<Service> services  , finalServices;
    Service[] confirmService;
    int[] count;
    List<Integer> amount;

    Context context;
    String startAt , endAt;
    int serviceCount, startHour , endHour , startMinute , endMinute , counter= 0, counterMinus=0;
    Double price , value ;
    public ServiceListAdapter(Context context ,int serviceCount,List<Service> services) {
        this.context = context;
        this.serviceCount = serviceCount;
        this.services = services;
        EventBus.getDefault().register(this);
        count = new int[serviceCount];
        confirmService = new Service[serviceCount];
    }


    @Subscribe
    public void onClickedEvent(ReserveButtonClickedServices clicked){
        finalServices= new ArrayList<>();
        amount = new ArrayList<>();
        for (int i = 0 ; i<serviceCount ; i++){
            if (confirmService[i]!=null){
                finalServices.add(confirmService[i]);
                amount.add(count[i]);
            }
        }
        if (finalServices.size()!=0)
        EventBus.getDefault().post(new PassServicesToFragment( finalServices , amount));

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.service_view , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        counter = 0;
        price = 0.0;
        if (services.get(position).getTitle_fa().equals("صبحانه")&&services.get(position).getHotel_id()==1){
            holder.title.setText("صبحانه رستوران اوپن");
            holder.image.setBackgroundResource( R.drawable.breackfast );
        }else if(services.get(position).getTitle_fa().equals("صبحانه")&&services.get(position).getHotel_id()==2){
            holder.title.setText("صبحانه رستوران گلکسی");
            holder.image.setBackgroundResource( R.drawable.breackfast );
        }
        else
        holder.title.setText(services.get(position).getTitle_fa());
        if (services.get(position).getService_id()==3)
            holder.image.setBackgroundResource( R.drawable.dinner );
        else if (services.get(position).getService_id()==227)
            holder.image.setBackgroundResource( R.drawable.pool );
        else if (services.get(position).getService_id()==228)
            holder.image.setBackgroundResource( R.drawable.spa );
        else if (services.get(position).getService_id()==2)
            holder.image.setBackgroundResource( R.drawable.lunch );
        else if (services.get(position).getService_id()==233)
            holder.image.setBackgroundResource( R.drawable.fast_track );
        else if (services.get(position).getService_id()==231)
            holder.image.setBackgroundResource( R.drawable.passport );
        else if (services.get(position).getService_id()==232)
            holder.image.setBackgroundResource( R.drawable.longe );
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int)services.get(position).getPrice());
        holder.price.setText( PersianDigitConverter.PerisanNumber(yourFormattedString+ " ريال"));
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
        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter.format(0);
        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString1));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    count[position]++;
                holder.minus.setBackgroundResource( R.drawable.ic_minus );
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String yourFormattedString = formatter.format(count[position]);
                    holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                    confirmService[position] = services.get(position);


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( count[position] != 0) {
                    count[position]--;
                    if (count[position] == 0){
                        holder.minus.setBackgroundResource( R.drawable.ic_minus_inactive );
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(0);
                        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                        confirmService[position] = null;count[position]=0;
                    }
                    else {

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(Integer.valueOf( count[position]) );
                        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                        confirmService[position]=services.get(position);

                    }
                }


            }
        });


    }
    @Override
    public int getItemCount() {
        return serviceCount-2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button plus;
        Button minus;
        TextView count;
        TextView title;
        TextView price;
        TextView time;
        ConstraintLayout image;

        ViewHolder(View itemView) {
            super(itemView);
            plus= itemView.findViewById( R.id.service_add);
            minus= itemView.findViewById( R.id.service_sub);
            count= itemView.findViewById( R.id.service_num);
            title= itemView.findViewById( R.id.service_title);
            price= itemView.findViewById( R.id.service_price);
            time= itemView.findViewById( R.id.service_time_table);
            image = itemView.findViewById( R.id.above );



        }
    }



}
