package ir.hotelairport.androidapp.airportHotels;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;

import ir.hotelairport.androidapp.Manifest;
import ir.hotelairport.androidapp.R;
import ir.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import ir.hotelairport.androidapp.airportHotels.api.data.HotelApi;
import ir.hotelairport.androidapp.airportHotels.api.data.VoucherController;
import okhttp3.ResponseBody;

import static io.fabric.sdk.android.Fabric.TAG;

public class DonePaymentFragment extends Fragment {
private static final int MY_PERMISION_REQUEST = 120;
    Button returnBut;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_done_payment ,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity() , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISION_REQUEST);
        }
        returnBut= view.findViewById(R.id.return_but);
        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherController voucherController = new VoucherController(callBack);
                voucherController.start(MyPreferenceManager.getInstace(getActivity()).getToken() ,String.valueOf(MyPreferenceManager.getInstace(getActivity()).getBookId()) );
                Intent intent = new Intent( getActivity() , MainActivity.class );
                getActivity().startActivity( intent );
                getActivity().finish();
            }
        });

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

                        startActivity(intent);
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
