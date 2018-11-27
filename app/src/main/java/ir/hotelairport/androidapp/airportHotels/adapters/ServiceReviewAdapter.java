package ir.hotelairport.androidapp.airportHotels.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.airportHotels.api.model.ServiceReview;

public class ServiceReviewAdapter extends RecyclerView.Adapter<ServiceReviewAdapter.ViewHolder> {
    List<ServiceReview> serviceReviews;
    private ArrayList<Integer> serviceId = new ArrayList<>();

    private Context context;
    private String startAt, endAt;
    private int startHour, endHour, startMinute, endMinute, index, pos;
    private boolean checked = true;

    public ServiceReviewAdapter(Context context, List<ServiceReview> serviceReviews, int pos) {
        this.context = context;
        this.serviceReviews = serviceReviews;
        this.pos = pos;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_review_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(serviceReviews.get(position).getService().getTitle_fa());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int) serviceReviews.get(position).getService().getPrice());
        holder.price.setText(PersianDigitConverter.PerisanNumber(yourFormattedString + "ريال"));
        startHour = serviceReviews.get(position).getService().getStart_access() / 100;
        startMinute = serviceReviews.get(position).getService().getStart_access() % 100;
        endHour = serviceReviews.get(position).getService().getEnd_access() / 100;
        endMinute = serviceReviews.get(position).getService().getEnd_access() % 100;

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

        if (serviceReviews.get(position).getService().getTitle_fa().equals("صبحانه") && serviceReviews.get(position).getService().getHotel_id() == 1) {
            holder.serviceCheck.setButtonDrawable(R.drawable.breakfast_checkbox);
        } else if (serviceReviews.get(position).getService().getTitle_fa().equals("صبحانه") && serviceReviews.get(position).getService().getHotel_id() == 2) {
            holder.serviceCheck.setButtonDrawable(R.drawable.breakfast_checkbox);
        }
        if (serviceReviews.get(position).getService().getTitle_fa().equals("صبحانه"))
            holder.serviceCheck.setButtonDrawable(R.drawable.breakfast_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("شام"))
            holder.serviceCheck.setButtonDrawable(R.drawable.dinner_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("استخر "))
            holder.serviceCheck.setButtonDrawable(R.drawable.pool_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("اسپا"))
            holder.serviceCheck.setButtonDrawable(R.drawable.spa_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("ناهار"))
            holder.serviceCheck.setButtonDrawable(R.drawable.lunch_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("فست تِرَک"))
            holder.serviceCheck.setButtonDrawable(R.drawable.fast_track_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("فست تِرَک و گذرنامه"))
            holder.serviceCheck.setButtonDrawable(R.drawable.passport_checkbox);
        if (serviceReviews.get(position).getService().getTitle_fa().equals("فست تِرَک و گذرنامه و لانژ"))
            holder.serviceCheck.setButtonDrawable(R.drawable.longe_checkbox);


    }

    @Override
    public int getItemCount() {
        return serviceReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox serviceCheck;

        TextView title;
        TextView price;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            serviceCheck = itemView.findViewById(R.id.service_check);
            title = itemView.findViewById(R.id.service_name);
            price = itemView.findViewById(R.id.service_price);
            time = itemView.findViewById(R.id.service_time_table);


        }
    }


}
