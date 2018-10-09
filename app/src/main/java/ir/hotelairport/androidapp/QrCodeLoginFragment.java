package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.util.List;

import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerRequest;
import ir.hotelairport.androidapp.Server.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class QrCodeLoginFragment extends Fragment implements BarcodeRetriever {
    BarcodeCapture barcodeCapture;
    TextView qr_guide;
    private DatabaseHandler db;
    SharedPreferences user_detail;
    public QrCodeLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qr_code_login, container, false);
        user_detail=getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        db=new DatabaseHandler(getActivity());
        barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);
        qr_guide=(TextView)view.findViewById(R.id.qr_guide_text);
        qr_guide.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"qr_guide"));

        return view;

    }



    public void onRetrieved(final Barcode barcode) {
        Log.i(TAG, "Barcode read: " + barcode.displayValue);
        LoginWithQr(barcode.displayValue);
    }

    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {

     /*   String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                "codes in frame include : \n";
        for (int index = 0; index < barcodeGraphics.size(); index++) {
            Barcode barcode = barcodeGraphics.get(index).getBarcode();
            message += (index + 1) + ". " + barcode.displayValue + "\n";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("code retrieved")
                .setMessage(message);
        builder.show();
*/

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // when image is scanned and processed
    }

    @Override
    public void onRetrievedFailed(String reason) {
        // in case of failure
    }

    @Override
    public void onPermissionRequestDenied() {

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        barcodeCapture.stopScanning();
    }
    private void LoginWithQr(String qr)
    {
        barcodeCapture.stopScanning();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setQr(qr);
        request.setDevice_type(1);
        request.setDevice_id(Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        status.getPermissionStatus().getEnabled();
        request.setToken(status.getSubscriptionStatus().getUserId());
        Call<ServerResponse> response = requestInterface.qrLogin(request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if (resp != null) {
                    Log.i("message", resp.getMessage());
                }
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            SharedPreferences.Editor editor = user_detail.edit();
                            editor.putString(Constants.JWT, "Bearer "+resp.getJwt());
                            editor.putBoolean(Constants.IS_LOGGED_IN, true);
                            editor.putString(Constants.USER_FIRST_NAME, resp.getProfile().getFirstname());
                            editor.putString(Constants.USER_LAST_NAME, resp.getProfile().getLastname());
                            editor.putString(Constants.PROFILE_IMAGE_NAME, resp.getProfile().getProfile_image());
                            editor.putInt(Constants.ROOM_NO, resp.getProfile().getRoom_no());
                            editor.apply();
                            Intent i = new Intent(getActivity(), LoggedInActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }
                        break;
                    case 401:
                        barcodeCapture.refresh();
                        Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"qr_code_not_valid"), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        barcodeCapture.refresh();
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                barcodeCapture.refresh();

                Log.d("error:",t.getMessage());
            }
        });
    }
}
