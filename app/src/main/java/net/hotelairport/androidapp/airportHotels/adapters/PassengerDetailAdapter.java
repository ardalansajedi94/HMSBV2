package net.hotelairport.androidapp.airportHotels.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import net.hotelairport.androidapp.R;

import net.hotelairport.androidapp.airportHotels.api.model.PaxReview;
import net.hotelairport.androidapp.airportHotels.api.model.ServiceReview;


public class PassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailAdapter.ViewHolder>{

    private Context context;
    private int index = 0;
    String [][] pos;
    List<PaxReview> paxReviews;
    List<String> paxId= new ArrayList<>();

    private ArrayList<ArrayList<Integer>> services = new ArrayList<>();
    private View view;
    public PassengerDetailAdapter(Context context , List<PaxReview> paxReview ,String[][] pos ) {
        this.context = context;
        this.paxReviews = paxReview;
        this.pos = pos;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passenger_review , parent , false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        List<ServiceReview> serviceList= new ArrayList<>();
        for (int i = 0; i<paxReviews.get(position).getServiceReviews().size();i++){
            serviceList.add(paxReviews.get(position).getServiceReviews().get(i));
        }
        ServiceReviewAdapter serviceListAdapter = new ServiceReviewAdapter(context  , serviceList , position );
        final LinearLayoutManager layout = new LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false);
        layout.setAutoMeasureEnabled(true);
        holder.recyclerView.setLayoutManager(layout );
        holder.recyclerView.setAdapter(serviceListAdapter);


        holder.name.setText(paxReviews.get(position).getFullName());
        holder.id.setText(paxReviews.get(position).getNationalCode());
        holder.email.setText(paxReviews.get(position).getEmail());
        holder.mobile.setText(paxReviews.get(position).getMobile());
        for (int i = 0 ; i<pos.length ; i++){
            for (int j=0 ; j < pos[i].length ; j++){
                if (pos[i][j]!=null)
                paxId.add(pos[i][j]);
            }
        }
        holder.paxId.setText(paxId.get(position));
        if (paxReviews.get(position).isDocType()){
            holder.natId.setText("کد ملی");
        }
        else {
            holder.natId.setText("شماره پاسپورت");
        }


    }


    @Override
    public int getItemCount() {
        return paxReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView id;
        TextView mobile;
        TextView email;
        TextView natId;
        TextView paxId;
        RecyclerView recyclerView;


        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById( R.id.edit_name);
            id = itemView.findViewById(R.id.edit_nat_code);
            mobile = itemView.findViewById(R.id.edit_cell_num);
            email = itemView.findViewById(R.id.edit_email);
            recyclerView = itemView.findViewById(R.id.service_list);
            natId = itemView.findViewById(R.id.txt3);
            paxId = itemView.findViewById(R.id.pax_id);
        }
    }


}