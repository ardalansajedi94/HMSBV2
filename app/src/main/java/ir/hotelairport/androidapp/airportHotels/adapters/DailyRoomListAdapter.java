package ir.hotelairport.androidapp.airportHotels.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.airportHotels.api.model.Price;
import ir.hotelairport.androidapp.airportHotels.api.model.Room;


public class DailyRoomListAdapter extends RecyclerView.Adapter<DailyRoomListAdapter.ViewHolder> {
    private List<Room> roomList;
    int[] count;
    int totalPrice, totalStay;
    Room[] confirmRoom;
    Context context;


    Boolean id, adult, room = true;
    int index = -1;
    getRooms getRooms;

    public static interface getRooms {
        void getRooms(int[] count, Room[] confirmRoom, int position);
    }

    public DailyRoomListAdapter(List<Room> roomList, Context context, getRooms getRooms) {
        this.roomList = roomList;
        this.context = context;
        this.getRooms = getRooms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_hotel_view, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        totalPrice = 0;
        totalStay = 0;
        holder.adultCount.setText(PersianDigitConverter.PerisanNumber(String.valueOf(roomList.get(position).getAdults())) + " نفر");

        String[] roomsCount = new String[]{PersianDigitConverter.PerisanNumber("0"), PersianDigitConverter.PerisanNumber("1"), PersianDigitConverter.PerisanNumber("2"), PersianDigitConverter.PerisanNumber("3"), PersianDigitConverter.PerisanNumber("4")};
        final ArrayAdapter<String> roomCountAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, roomsCount);
        count = new int[roomList.size()];
        confirmRoom = new Room[roomList.size()];
        if (roomList.get(position).getStar() == 2) {
            holder.star.setBackgroundResource(R.drawable.star_2);
        } else if (roomList.get(position).getStar() == 3) {
            holder.star.setBackgroundResource(R.drawable.star_3);
        } else if (roomList.get(position).getStar() == 4) {
            holder.star.setBackgroundResource(R.drawable.star_4);
        } else if (roomList.get(position).getStar() == 5) {
            holder.star.setBackgroundResource(R.drawable.star_5);
        } else {
            holder.star.setBackgroundResource(R.drawable.star_1);
        }
        MonthPriceAdapter monthPriceAdapter = new MonthPriceAdapter(roomList.get(position).getPrice());

        holder.prices.setAdapter(monthPriceAdapter);
        holder.prices.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        List<Price> prices = new ArrayList<>();
        prices = roomList.get(position).getPrice();
        for (int i = 0; i < prices.size(); i++) {
            totalPrice = totalPrice + (prices.get(i).getNights() * prices.get(i).getPriceByMonth());
            totalStay = totalStay + prices.get(i).getNights();
        }
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(totalPrice);
        holder.price.setText(PersianDigitConverter.PerisanNumber(yourFormattedString) + " ریال");
        holder.stay.setText(PersianDigitConverter.PerisanNumber(String.valueOf(totalStay)));
        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter.format(0);
        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString1));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[position] < 4) {
                    count[position]++;
                    holder.minus.setBackgroundResource(R.drawable.ic_minus);
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String yourFormattedString = formatter.format(count[position]);
                    holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                    confirmRoom[position] = roomList.get(position);
                    getRooms.getRooms(count, confirmRoom, position);
                }
            }


        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count[position] != 0) {
                    count[position]--;
                    if (count[position] == 0) {
                        holder.minus.setBackgroundResource(R.drawable.ic_minus_inactive);
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(0);
                        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                        confirmRoom[position] = null;
                    } else {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(Integer.valueOf(count[position]));
                        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                        confirmRoom[position] = roomList.get(position);
                    }
                    getRooms.getRooms(count, confirmRoom, position);

                }


            }
        });


        if (roomList.get(position).getHotel_title_en().equals("Ibis")) {
            holder.hotelName.setText("هتل ایبیس");
            holder.hotelPic.setBackgroundResource(R.drawable.ibis);
        } else {
            holder.hotelName.setText("هتل نووتل");
            holder.hotelPic.setBackgroundResource(R.drawable.novotel);
        }


    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                adultCount,
                price, stay, hotelName;
        ImageView star;
        Button plus, minus;
        RecyclerView prices;
        TextView count;
        ConstraintLayout hotelPic;

        ViewHolder(View itemView) {
            super(itemView);
            adultCount = itemView.findViewById(R.id.adult_count);
            star = itemView.findViewById(R.id.star);
            count = itemView.findViewById(R.id.room_count);
            price = itemView.findViewById(R.id.price);
            hotelName = itemView.findViewById(R.id.hotel_name);
            plus = itemView.findViewById(R.id.service_add);
            minus = itemView.findViewById(R.id.service_sub);
            prices = itemView.findViewById(R.id.prices);
            stay = itemView.findViewById(R.id.time_stay);
            hotelPic = itemView.findViewById(R.id.above);
        }
    }


}
