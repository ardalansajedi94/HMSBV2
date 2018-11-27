package ir.hotelairport.androidapp;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.hotelairport.androidapp.Adapters.InvoicesListAdapter;
import ir.hotelairport.androidapp.Models.Invoice;
import ir.hotelairport.androidapp.Models.Profile;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;
import ir.hotelairport.androidapp.Server.RequestInterface;
import ir.hotelairport.androidapp.Server.ServerResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView name_tv, room_no_tv, request_invoice_hint;
    ListView invoices_list;
    Button request_invoice_btn;
    CircleImageView profile_image;
    private Profile profile;
    private InvoicesListAdapter invoicesListAdapter;
    private ArrayList<Invoice> invoicesContent;
    private SharedPreferences user_detail;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progress;
    private ImageLoader imageLoader;
    DisplayImageOptions options;
    Animation profileImageBlinkingAnimation;
    DatabaseHandler db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 123) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (requestCode == 133) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (requestCode == 123) {
                Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "write_perm_hint"), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = new DatabaseHandler(getActivity());
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .build();
        imageLoader = ImageLoader.getInstance();
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        name_tv = (TextView) view.findViewById(R.id.NameTV);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        room_no_tv = (TextView) view.findViewById(R.id.RoomNoTV);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        request_invoice_btn = (Button) view.findViewById(R.id.request_invoice_btn);
        invoices_list = (ListView) view.findViewById(R.id.InvoicesList);
        getProfile(false);
        request_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request_invoice();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_detail.getInt(Constants.USER_UNDERSTOOD_PROFILE_IMAGE, 0) == 0) {
                    SharedPreferences.Editor editor = user_detail.edit();
                    editor.putInt(Constants.USER_UNDERSTOOD_PROFILE_IMAGE, 1);
                    editor.apply();
                    profile_image.clearAnimation();
                }
                final Integer[] ids = {1, 2, 3};
                CharSequence[] items = {db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "from_camera"), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "from_gallery"), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "cancel")};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "change_profile_photo_desc"));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = ids[which];
                        switch (id) {
                            case 1:
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(takePicture, 0);
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                                    }
                                } else {
                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 0);
                                }


                                break;
                            case 2:
                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto, 1);
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 133);
                                    }
                                } else {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);
                                }

                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        if (user_detail.getInt(Constants.USER_UNDERSTOOD_PROFILE_IMAGE, 0) == 0) {
            profileImageBlinkingAnimation = new AlphaAnimation(1, 0);
            profileImageBlinkingAnimation.setDuration(1000);
            profileImageBlinkingAnimation.setInterpolator(new LinearInterpolator());
            profileImageBlinkingAnimation.setRepeatCount(Animation.INFINITE);
            profileImageBlinkingAnimation.setRepeatMode(Animation.REVERSE);
            profile_image.startAnimation(profileImageBlinkingAnimation);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProfile(true);
            }
        });
        request_invoice_btn.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "request_invoice"));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "HMS");
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {

                            Log.d("CustomCameraApp", "failed to create directory");
                        }
                    }
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile_image.setImageBitmap(thumbnail);
                    change_profile_photo(Uri.fromFile(destination));
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    profile_image.setImageURI(selectedImage);
                    change_profile_photo(selectedImage);
                }
                break;
        }
    }

    private void getProfile(final boolean is_refreshing) {
        if (!is_refreshing) {
            progress = new ProgressDialog(getActivity());
            progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response;
        if (user_detail.getInt(Constants.LANGUAGE_ID, 1) == 1)
            response = requestInterface.get_profile(user_detail.getString(Constants.JWT, ""), 1);
        else
            response = requestInterface.get_profile(user_detail.getString(Constants.JWT, ""), 2);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (!is_refreshing) {
                    progress.dismiss();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                ServerResponse resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {

                            invoicesContent = new ArrayList<Invoice>();
                            Invoice fist_row = new Invoice();
                            invoicesContent.add(fist_row);
                            profile = resp.getProfile();
                            File imageFile = imageLoader.getDiscCache().get(Constants.MEDIA_BASE_URL + profile.getProfile_image());
                            if (imageFile.exists()) {
                                imageFile.delete();
                            }
                            SharedPreferences.Editor editor = user_detail.edit();
                            editor.putString(Constants.PROFILE_IMAGE_NAME, resp.getProfile().getProfile_image());
                            editor.putInt(Constants.ROOM_NO, resp.getProfile().getRoom_no());
                            editor.apply();
                            DiskCacheUtils.removeFromCache(Constants.MEDIA_BASE_URL + profile.getProfile_image(), ImageLoader.getInstance().getDiskCache());
                            MemoryCacheUtils.removeFromCache(Constants.MEDIA_BASE_URL + profile.getProfile_image(), ImageLoader.getInstance().getMemoryCache());
                            invoicesContent.addAll(resp.getInvoices());
                            invoicesListAdapter = new InvoicesListAdapter(invoicesContent, getActivity());
                            invoices_list.setAdapter(invoicesListAdapter);
                            name_tv.setText(profile.getFirstname() + " " + profile.getLastname());
                            if (profile.getRoom_no() != 0)
                                room_no_tv.setText(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "room_no") + " " + String.valueOf(profile.getRoom_no()));
                            else {
                                room_no_tv.setVisibility(View.INVISIBLE);
                                request_invoice_btn.setVisibility(View.GONE);
                                invoices_list.setVisibility(View.GONE);
                            }
                            Picasso mPicasso = Picasso.with(getActivity());
                            mPicasso.load(Constants.MEDIA_BASE_URL + profile.getProfile_image()).skipMemoryCache().placeholder(R.drawable.ic_person).into(profile_image);
                            // imageLoader.displayImage(Constants.MEDIA_BASE_URL+profile.getProfile_image(),profile_image,options);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (!is_refreshing) {
                    progress.dismiss();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Log.d("error:", t.getMessage());
            }
        });
    }

    private void request_invoice() {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "connecting_to_server"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ServerResponse> response = requestInterface.request_invoice(user_detail.getString(Constants.JWT, ""));
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                Log.d("response", String.valueOf(response.code()));
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "send_success"), Toast.LENGTH_SHORT).show();
                            request_invoice_btn.setEnabled(false);
                            getProfile(false);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:", t.getMessage());
            }
        });

    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void change_profile_photo(Uri image_uri) {
        progress = new ProgressDialog(getActivity());
        progress.setMessage(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "uploading"));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        File image = new File(getRealPathFromURI(image_uri));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("profile_image", image.getName(), requestFile);

        Call<ServerResponse> response = requestInterface.upload_profile_picture(user_detail.getString(Constants.JWT, ""), body);
        RetrofitWithRetry.enqueueWithRetry(response, 3, new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                progress.dismiss();
                ServerResponse resp = response.body();
                Log.d("response", String.valueOf(response.code()));
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "uploaded"), Toast.LENGTH_SHORT).show();
                            Intent intent = getActivity().getIntent();
                            getActivity().finish();
                            startActivity(intent);
                        }
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID, 1), "server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:", t.getMessage());
            }
        });

    }

}
