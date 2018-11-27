package ir.hotelairport.androidapp.airportHotels.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.airportHotels.api.model.Service;

public class ServiceStayBookAdapter extends RecyclerView.Adapter<ServiceStayBookAdapter.ViewHolder> {
    private ArrayList<Service> services;
    private ArrayList<Service> serviceId = new ArrayList<>();

    private Context context;
    private String startAt, endAt;
    private int serviceCount, startHour, endHour, startMinute, endMinute, index, pos;
    private boolean checked = true;
    private myCheckedService myCheckedService;


    public static interface myCheckedService {
        void onItemCheck(ArrayList<Service> serviceId, int position);
    }

    public ServiceStayBookAdapter(Context context, int serviceCount, ArrayList<Service> services, @NonNull myCheckedService myCheckedService, int pos) {
        this.context = context;
        this.serviceCount = serviceCount;
        this.services = services;
        this.myCheckedService = myCheckedService;
        this.pos = pos;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_stay_book_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(services.get(position).getTitle_fa());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int) services.get(position).getPrice());
        holder.price.setText(PersianDigitConverter.PerisanNumber(yourFormattedString + "ريال"));
        startHour = services.get(position).getStart_access() / 100;
        startMinute = services.get(position).getStart_access() % 100;
        endHour = services.get(position).getEnd_access() / 100;
        endMinute = services.get(position).getEnd_access() % 100;

        if (startHour == 0) {
            startAt = PersianDigitConverter.PerisanNumber("00") + ":" + PersianDigitConverter.PerisanNumber(String.valueOf(startMinute));
            if (startMinute == 0) {
                startAt = PersianDigitConverter.PerisanNumber("00") + ":" + PersianDigitConverter.PerisanNumber("00");
            }
        } else if (startMinute == 0) {
            startAt = PersianDigitConverter.PerisanNumber(String.valueOf(startHour)) + ":" + PersianDigitConverter.PerisanNumber("00");
        } else {
            startAt = PersianDigitConverter.PerisanNumber(String.valueOf(startHour)) + ":" + PersianDigitConverter.PerisanNumber(String.valueOf(startMinute));
        }
        if (endHour == 0) {
            endAt = PersianDigitConverter.PerisanNumber("00") + ":" + PersianDigitConverter.PerisanNumber(String.valueOf(endMinute));
            if (endMinute == 0) {
                endAt = PersianDigitConverter.PerisanNumber("00") + ":" + PersianDigitConverter.PerisanNumber("00");
            }
        } else if (endMinute == 0) {
            endAt = PersianDigitConverter.PerisanNumber(String.valueOf(endHour)) + ":" + PersianDigitConverter.PerisanNumber("00");
        } else {
            endAt = PersianDigitConverter.PerisanNumber(String.valueOf(endHour)) + ":" + PersianDigitConverter.PerisanNumber(String.valueOf(endMinute));
        }
        holder.time.setText(startAt + " الی " + endAt);
        holder.serviceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.serviceCheck.isChecked()) {
                    holder.serviceCheck.setChecked(false);
                } else {
                    holder.serviceCheck.setChecked(true);
                    holder.serviceCheck.animate();
                }
            }
        });

        holder.serviceCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serviceId.add(services.get(position));
                } else {
                    for (int i = 0; i < serviceId.size(); i++) {
                        if (services.get(position).getService_id() == serviceId.get(i).getService_id()) {
                            checked = false;
                            index = i;
                        }
                    }
                    if (!checked) {
                        serviceId.remove(index);
                    }
                    checked = true;
                }
                myCheckedService.onItemCheck(serviceId, pos);
            }
        });

        if (services.get(position).getTitle_fa().equals("صبحانه"))
            holder.serviceCheck.setButtonDrawable(R.drawable.breakfast_checkbox);
        if (services.get(position).getTitle_fa().equals("شام"))
            holder.serviceCheck.setButtonDrawable(R.drawable.dinner_checkbox);
        if (services.get(position).getTitle_fa().equals("استخر "))
            holder.serviceCheck.setButtonDrawable(R.drawable.pool_checkbox);
        if (services.get(position).getTitle_fa().equals("اسپا"))
            holder.serviceCheck.setButtonDrawable(R.drawable.spa_checkbox);
        if (services.get(position).getTitle_fa().equals("ناهار"))
            holder.serviceCheck.setButtonDrawable(R.drawable.lunch_checkbox);
        if (services.get(position).getTitle_fa().equals("فست تِرَک"))
            holder.serviceCheck.setButtonDrawable(R.drawable.fast_track_checkbox);
        if (services.get(position).getTitle_fa().equals("فست تِرَک و گذرنامه"))
            holder.serviceCheck.setButtonDrawable(R.drawable.passport_checkbox);
        if (services.get(position).getTitle_fa().equals("فست تِرَک و گذرنامه و لانژ"))
            holder.serviceCheck.setButtonDrawable(R.drawable.longe_checkbox);
    }

    @Override
    public int getItemCount() {
        return serviceCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox serviceCheck;
        TextView title;
        TextView price;
        TextView time;
        ConstraintLayout serviceCard;

        ViewHolder(View itemView) {
            super(itemView);
            serviceCheck = itemView.findViewById(R.id.service_check);
            serviceCard = itemView.findViewById(R.id.service_card);
            title = itemView.findViewById(R.id.service_name);
            price = itemView.findViewById(R.id.service_price);
            time = itemView.findViewById(R.id.service_time_table);


        }
    }


}
