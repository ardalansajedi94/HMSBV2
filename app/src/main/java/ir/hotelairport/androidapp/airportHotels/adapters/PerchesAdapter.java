package ir.hotelairport.androidapp.airportHotels.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.MainActivity;
import ir.hotelairport.androidapp.airportHotels.PersianDigitConverter;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.VoucherController;
import ir.hotelairport.androidapp.airportHotels.api.model.Voucher;
import okhttp3.ResponseBody;
import saman.zamani.persiandate.PersianDateFormat;


public class PerchesAdapter extends RecyclerView.Adapter<PerchesAdapter.ViewHolder>{

    List<Voucher> voucherList;
    Context context;
    public PerchesAdapter(List<Voucher> voucherList , Context context) {
        this.voucherList = voucherList;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perches_view , parent , false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (voucherList.get(position).getData().getType() == 0){
            holder.title.setText("سرویس");
            try {
                PersianDateFormat pdformater = new PersianDateFormat();
                saman.zamani.persiandate.PersianDate jdate ;
                saman.zamani.persiandate.PersianDate odate ;

                jdate =pdformater.parseGrg(voucherList.get(position).getData().getCheckIn(),"yyyy-MM-dd HH:mm:ss");
                odate =pdformater.parseGrg(voucherList.get(position).getData().getCheckOut(),"yyyy-MM-dd HH:mm:ss");
                holder.checkIn.setText(PersianDigitConverter.PerisanNumber( jdate.getShYear() + "/" + jdate.getShMonth() + "/" + jdate.getShDay() ));
                holder.checkOut.setText(PersianDigitConverter.PerisanNumber( odate.getShYear() + "/" + odate.getShMonth() + "/" + odate.getShDay() ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (voucherList.get(position).getData().getType() == 1){
            holder.title.setText("کوتاه مدت");
            try {
                PersianDateFormat pdformater = new PersianDateFormat();
                saman.zamani.persiandate.PersianDate jdate ;
                holder.CheckOutText.setText("مدت اقامت");

                jdate =pdformater.parseGrg(voucherList.get(position).getData().getCheckIn(),"yyyy-MM-dd");

                holder.checkIn.setText(PersianDigitConverter.PerisanNumber( jdate.getShYear() + "/" + jdate.getShMonth() + "/" + jdate.getShDay() ));
                if (voucherList.get(position).getData().getCheckOut().equals("3"))
                holder.checkOut.setText(PersianDigitConverter.PerisanNumber( "3" ) + "ساعته");
                else
                    holder.checkOut.setText(PersianDigitConverter.PerisanNumber( "6" ) + "ساعته");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (voucherList.get(position).getData().getType() == 2){
            try {
                PersianDateFormat pdformater = new PersianDateFormat();
                saman.zamani.persiandate.PersianDate jdate ;
                saman.zamani.persiandate.PersianDate odate ;

                jdate =pdformater.parseGrg(voucherList.get(position).getData().getCheckIn(),"yyyy-MM-dd");
                odate =pdformater.parseGrg(voucherList.get(position).getData().getCheckOut(),"yyyy-MM-dd");
                holder.checkIn.setText(PersianDigitConverter.PerisanNumber( jdate.getShYear() + "/" + jdate.getShMonth() + "/" + jdate.getShDay() ));
                holder.checkOut.setText(PersianDigitConverter.PerisanNumber( odate.getShYear() + "/" + odate.getShMonth() + "/" + odate.getShDay() ));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.title.setText("روزانه");
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format((int)voucherList.get(position).getData().getTotalPrice());
        holder.price.setText(PersianDigitConverter.PerisanNumber(yourFormattedString+ " ريال"));

        holder.voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoucherController voucherController = new VoucherController(callBack);

                voucherController.start(MyPreferenceManager.getInstace(context).getLoginRes().getToken_type() +" "+ MyPreferenceManager.getInstace(context).getLoginRes().getAccess_token() ,String.valueOf(voucherList.get(holder.getAdapterPosition()).getBook_id()) );

            }
        });

    }
    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title , checkIn , checkOut , price , CheckOutText;
        Button voucher;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            checkIn = itemView.findViewById(R.id.check_in_text);
            checkOut = itemView.findViewById(R.id.check_out_text);
            CheckOutText = itemView.findViewById(R.id.check_out);
            price = itemView.findViewById(R.id.price_text);
            voucher = itemView.findViewById(R.id.voucher);


        }
    }

    HotelApi.VoucherCallBack callBack = new HotelApi.VoucherCallBack() {
        @Override
        public void onResponse(final ResponseBody responseBody) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); ;
                        File file = new File(path, "voucher.pdf");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        IOUtils.write(responseBody.bytes(), fileOutputStream);


                        MimeTypeMap map = MimeTypeMap.getSingleton();
                        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                        String type = map.getMimeTypeFromExtension(ext);

                        if (type == null)
                            type = ".pdf";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri data = Uri.fromFile(file);

                        intent.setDataAndType(data, type);

                        ((MainActivity)context).startActivity(intent);
                    }
                    catch (Exception ex){
                    }
                    return null;
                }
            }.execute();

        }

        @Override
        public void onFailure(String cause) {

        }

        @Override
        public void onError(String cause) {

        }
    };

}
