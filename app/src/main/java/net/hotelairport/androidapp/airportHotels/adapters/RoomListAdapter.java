package net.hotelairport.androidapp.airportHotels.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import net.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.api.model.Room;
import net.hotelairport.androidapp.airportHotels.api.model.Service;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder>{
    private List<Room> roomList;
    int[] count ;
    Room[] confirmRoom;
    Context context;


    Boolean id , adult ,room=true;
    int index=-1;
    getRooms getRooms;
    public  static interface getRooms{
        void getRooms(int[] count , Room[] confirmRoom ,int position);
    }
    public RoomListAdapter(List<Room> roomList , Context context, getRooms getRooms) {
        this.roomList = roomList;
        this.context = context;
        this.getRooms = getRooms;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_view , parent , false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.adultCount.setText( PersianDigitConverter.PerisanNumber(String.valueOf(roomList.get(position).getAdults())) + " نفر");
        if (roomList.get( position ).getHotel_title_en().equals( "Ibis" )){
            holder.hotelName.setText("هتل ایبیس" );
            holder.hotelPic.setBackgroundResource( R.drawable.ibis );
        }
        else {
            holder.hotelName.setText("هتل نووتل" );
            holder.hotelPic.setBackgroundResource( R.drawable.novotel );
        }
        String[] roomsCount = new String[]{PersianDigitConverter.PerisanNumber("0"),PersianDigitConverter.PerisanNumber("1") , PersianDigitConverter.PerisanNumber("2") , PersianDigitConverter.PerisanNumber("3") , PersianDigitConverter.PerisanNumber("4") };
        final ArrayAdapter<String> roomCountAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, roomsCount);
        count = new int[roomList.size()];
        confirmRoom = new Room[roomList.size()];
        if (roomList.get(position).getStar() == 2){
            holder.star.setBackgroundResource( R.drawable.star_2);
        }else if(roomList.get(position).getStar() == 3){
            holder.star.setBackgroundResource(R.drawable.star_3);
        }else if(roomList.get(position).getStar() == 4){
            holder.star.setBackgroundResource(R.drawable.star_4);
        }else if(roomList.get(position).getStar() == 5){
            holder.star.setBackgroundResource(R.drawable.star_5);
        }else{
            holder.star.setBackgroundResource(R.drawable.star_1);
        }
       ArrayList<Service> serveList = roomList.get(position).getServices();

        String serNames = "";

        for(int i = 0; i < serveList.size(); i++){
            if(serveList.get(i).getService_id() !=234 &&serveList.get(i).getService_id() !=235)
            serNames += serveList.get(i).getTitle_fa() + " - ";
        }
        if (serNames.length()>1)
            serNames = serNames.substring(0 , serNames.length() - 3);
        if (serNames.length()== 0 )
            serNames = "سرویسی در این ساعت موجود نیست";
        holder.services.setText(serNames);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int)roomList.get(position).getPrice().get(0).getPriceByMonth());
        holder.price.setText(PersianDigitConverter.PerisanNumber(yourFormattedString+ " ريال"));
        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter.format(0);
        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString1));
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count[position]++;
                DecimalFormat formatter = new DecimalFormat( "#,###,###" );
                holder.minus.setBackgroundResource( R.drawable.ic_minus );
                String yourFormattedString = formatter.format( count[position] );
                holder.count.setText( PersianDigitConverter.PerisanNumber( yourFormattedString ) );
                confirmRoom[position] = roomList.get( position );
                getRooms.getRooms( count ,confirmRoom  , position);
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
                        confirmRoom[position] = null;
                    }
                    else {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(Integer.valueOf( count[position]) );
                        holder.count.setText(PersianDigitConverter.PerisanNumber(yourFormattedString));
                        confirmRoom[position] = roomList.get( position );


                    }
                    getRooms.getRooms( count ,confirmRoom , position);

                }


            }
        });



    }
    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                 adultCount,
                 price, services;
        ImageView star ;
        ConstraintLayout hotelPic;
        Button plus , minus;
        TextView count,hotelName;
        ViewHolder(View itemView) {
            super(itemView);
            adultCount = itemView.findViewById(R.id.adult_count);
            services = itemView.findViewById(R.id.services);
            star = itemView.findViewById(R.id.star);
            count = itemView.findViewById(R.id.room_count);
            price = itemView.findViewById(R.id.price);
            hotelName=itemView.findViewById( R.id.hotel_name );
            plus= itemView.findViewById( R.id.service_add);
            minus= itemView.findViewById( R.id.service_sub);
            hotelPic = itemView.findViewById( R.id.above );


        }
    }



}
