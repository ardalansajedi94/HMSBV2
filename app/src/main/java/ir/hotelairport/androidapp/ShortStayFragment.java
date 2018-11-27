package ir.hotelairport.androidapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.List;

import ir.hotelairport.androidapp.Adapters.ServiceOnlyListAdapter;
import ir.hotelairport.androidapp.Models.Service;
import ir.hotelairport.androidapp.SQLiteDB.DatabaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShortStayFragment extends Fragment implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    Spinner stayHoursSpinner;
    Spinner personCountSpinner;
    TextView policyTv;
    Button date_tv, time_tv;
    NonScrollableListView servicesList;
    ProgressDialog progress;
    Button byeBtn;
    private DatabaseHandler db;
    private SharedPreferences user_detail;
    private ArrayList<Service> services;
    private ServiceOnlyListAdapter serviceOnlyListAdapter;
    private static final String TIMEPICKER = "TimePickerDialog", DATEPICKER = "DatePickerDialog";

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        time_tv.setText(time);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
        date_tv.setText(date);
    }

    public ShortStayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_short_stay, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("اقامت کوتاه مدت");
        db = new DatabaseHandler(getActivity());
        user_detail = getActivity().getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        policyTv = (TextView) view.findViewById(R.id.policyTv);
        servicesList = view.findViewById(R.id.servicesList);
        byeBtn = view.findViewById(R.id.send_req_btn);
        date_tv = (Button) view.findViewById(R.id.date_tv);
        time_tv = (Button) view.findViewById(R.id.time_tv);
        policyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalGuideFragment guideFragment = ModalGuideFragment.newInstance("قوانین هتل", "مهمان باید در هنگام ورود به هتل برگ تأییدیه\u200Cی رزرو را به  پذیرش ارائه دهد.\n" +
                        "ذخیره\u200Cی اتاق بنابر درخواست متقاضی و یا دفتر خدمات مسافرتی، به نام مهمان استفاده\u200Cکننده صادر می\u200Cشود و غیرقابل انتقال، تغییر و استرداد است.\n" +
                        "در هنگام ورود به هتل، مهمانی که اتاق به نام ایشان رزرو شده\u200Cاست باید شناسنامه، کارت ملی و یا گذرنامه معتبر خود و سایر همراهان را برای دریافت اتاق به پذیرش نشان دهد. لازم به ذکر است برای پذیرش زوج\u200Cهای ایرانی حتماً یکی از زوجین باید دارای شناسنامه باشند. پذیرش نفر دوم با کارت ملی یا پاسپورت بلامانع می\u200Cباشد.\n" +
                        "صیغه\u200Cنامه باید دارای مهر برجسته\u200Cی قوه\u200Cی قضاییه باشد و مهر دفترخانه یا عاقد به تنهایی مورد قبول نمی\u200Cباشد. در صورت ارائه\u200Cی صیغه\u200Cنامه حتماً زوجه باید دارای شناسنامه با توضیحات طلاق یا فوت در صفحه\u200Cی دوم باشد در غیر این صورت فقط با توضیحات و مجوز پدر زوجه در صیغه\u200Cنامه اتاق تحویل خواهدشد. برای آقا وجود شناسنامه الزامی نیست، کارت ملی یا پاسپورت نیز مورد قبول است.\n" +
                        "در صورت ارائه\u200Cی هرگونه مدارک هویتی و اطلاعات نادرست، عواقب ناشی از آن بر عهده\u200Cی مهمان است.\n" +
                        "هتل در خصوص جا ماندن، تأخیر و یا لغو برنامه\u200Cی حرکت وسیله\u200Cی سفر و یا پرواز مهمانان هیچ\u200Cگونه مسؤولیتی ندارد.\n" +
                        "خسارات وارده به اموال و اثاثیه هتل به عهده\u200Cی مهمان است.\n" +
                        "اقامت مهمانان در هتل مطابق تاریخ و ساعت مندرج در تأییدیه\u200Cی رزرو بوده و در صورت اقامت بیشتر از ساعات رزروشده می\u200Cبایست با پرداخت مابه\u200Cالتفاوت نرخ پرداخت\u200Cشده توسط میهمان با نرخ اقامت کامل (یک شب) هتل، انجام پذیرد.\n" +
                        "چنانچه مهمان در زمان مندرج در واچر به هتل مراجعه نکرد و یا تا 24 ساعت قبل از ساعت ورود به صورت مکتوب علت عدم حضور خود را به واحد رزراسیون اعلام نکرد، رزرو انجام\u200Cشده باطل شده و هیچ مبلغی مسترد نمی\u200Cگردد.\n" +
                        "چنانچه مهمان پس از رزرو و اقامت در هتل، بنا به هر دلیلی از ادامه\u200Cی اقامت در هتل منصرف شد، هیچ هزینه\u200Cای مسترد نخواهدشد.\n" +
                        "ملاک قیمت\u200Cها در سیستم بر اساس نرخ نمایش\u200Cداده\u200Cشده در سایت در لحظه\u200Cی ثبت رزرو خواهدبود و در بازه\u200Cهای زمانی مختلف ممکن است متغیر باشد.\n" +
                        "حداقل سن الزامی جهت اخذ اتاق 18 سال تمام می\u200Cباشد و در صورتی که فرد رزروکننده دارای سن کمتری باشد رزرو ابطال می\u200Cگردد و هزینه مسترد نخواهدشد.\n" +
                        "در صورت عدم رعایت قوانین مندرج در سایت، هتل هیچ مسئولیتی نسبت به بازپرداخت وجه نخواهدداشت.\n" +
                        "امکان برگزاری جلسات در اتاق\u200Cها و سوییت\u200Cها وجود ندارد و در صورت تمایل به برگزاری باید به واحد فروش و بازاریابی هتل مراجعه شود.\n" +
                        "کلیه\u200Cی مهمانان ملزم به رعایت شئونات اسلامی و قوانین داخلی هتل می\u200Cباشند.\n" +
                        "جهت هر گونه هماهنگی و توضیحات تکمیلی با شماره تلفن 02126404026 و یا ایمیل info@hotelairport.ir تماس بگیرید.\n" +
                        "تعداد نفرات باید مطابق با رزرو باشد و نفر اضافی باید اتاق اضافی بگیرد.\n" +
                        "هزینه\u200Cهای اکسترا به صورت نقدی (ریال، دلار، یورو) پرداخت می\u200Cگردد.\n" +
                        "ارائه\u200Cی پرینت واچر به رسپشن (پذیرش) الزامی است.");
                FragmentManager fm = getChildFragmentManager();
                guideFragment.show(fm, "fragment_guide");
            }
        });
        getServices();
        serviceOnlyListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showInvoicePrice();
            }
        });
        time_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_detail.getInt(Constants.LANGUAGE_ID, 0) == 1) {

                    PersianCalendar now = new PersianCalendar();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(
                            ShortStayFragment.this,
                            now.get(PersianCalendar.HOUR_OF_DAY),
                            now.get(PersianCalendar.MINUTE),
                            true
                    );
                    tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Log.d(TIMEPICKER, "Dialog was cancelled");
                        }
                    });
                    tpd.show(getActivity().getFragmentManager(), TIMEPICKER);
                }

            }
        });
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user_detail.getInt(Constants.LANGUAGE_ID, 1) == 1) {
                    PersianCalendar now = new PersianCalendar();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            ShortStayFragment.this,
                            now.getPersianYear(),
                            now.getPersianMonth(),
                            now.getPersianDay()
                    );
                    dpd.show(getActivity().getFragmentManager(), DATEPICKER);
                }

            }
        });
        servicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service service = services.get(position);
                ModalGuideFragment modalGuideFragment = ModalGuideFragment.newInstance(service.getName(), service.getDesc());
                FragmentManager fm = getChildFragmentManager();
                modalGuideFragment.show(fm, "fragment_guide");
            }
        });
        List<String> personCountList = new ArrayList<String>();
        personCountList.add("1 نفر");
        personCountList.add("2 نفر");
        ArrayAdapter<String> personCountAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, personCountList);
        personCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personCountSpinner.setAdapter(personCountAdapter);

        List<String> stayHoursList = new ArrayList<String>();
        stayHoursList.add("3 ساعت");
        stayHoursList.add("6 ساعت");
        ArrayAdapter<String> stayHoursAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stayHoursList);
        stayHoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stayHoursSpinner.setAdapter(stayHoursAdapter);
        return view;
    }

    private void showInvoicePrice() {
        int price = 0;
        for (int i = 0; i < services.size(); i++) {
            price = price + (services.get(i).getCount() * services.get(i).getPrice_with_discount());
        }
        byeBtn.setText("خرید(" + String.valueOf(price) + ")");
    }

    private void getServices() {
        services = new ArrayList<>();
        Service service = new Service();
        service.setName("استخر");
        service.setCode(100);
        service.setCount(0);
        service.setPrice(10000);
        service.setPrice_with_discount(90000);
        service.setDesc("توضیحات مربوط به استخر");
        services.add(service);
        service = new Service();
        service.setName("تست");
        service.setCode(101);
        service.setCount(0);
        service.setPrice(10000);
        service.setPrice_with_discount(10000);
        service.setDesc("توضیحات مربوط به تست");
        services.add(service);
        serviceOnlyListAdapter = new ServiceOnlyListAdapter(services, getActivity());
        servicesList.setAdapter(serviceOnlyListAdapter);
        /*
        progress = new ProgressDialog(getActivity());
        progress.setMessage("در حال دریافت لیست خدمات...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HOTEL_AIR_PORT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setApi_token(Constants.API_TOKEN);
        Call<Map<String,Service>> response = requestInterface.getServicesPrice(request);
        RetrofitWithRetry.enqueueWithRetry(response,3,new Callback<Map<String,Service>>() {
            @Override
            public void onResponse(Call<Map<String,Service>> call, retrofit2.Response<Map<String,Service>> response) {
                progress.dismiss();
                Map<String,Service> resp = response.body();
                switch (response.code()) {
                    case 200:
                        if (resp != null) {
                            services = new ArrayList<>();
                            for (Map.Entry<String, Service> entry : resp.entrySet())
                            {
                                Service item;
                                switch (entry.getValue().getName()){
                                    case "swimming_pool":
                                        item = entry.getValue();
                                        item.setName("استخر");
                                        services.add(item);
                                        break;
                                    case "massage":
                                        item = entry.getValue();
                                        item.setName("ماساژ");
                                        services.add(item);
                                        break;
                                    case "food_breakfast_ibis1pax":
                                        item = entry.getValue();
                                        item.setName("صبحانه رستوران اوپن");
                                        services.add(item);
                                        break;
                                    case "food_breakfast_novotel1pax":
                                        item = entry.getValue();
                                        item.setName("صبحانه رستوران گلکسی");
                                        services.add(item);
                                        break;
                                    case "food_lunch1pax":
                                        item = entry.getValue();
                                        item.setName("ناهار");
                                        services.add(item);
                                        break;
                                    case "food_dinner1pax":
                                        item = entry.getValue();
                                        item.setName("شام");
                                        services.add(item);
                                        break;
                                    case "fasttrack":
                                        item = entry.getValue();
                                        item.setName("فست ترک");
                                        services.add(item);
                                        break;
                                }

                            }
                            serviceOnlyListAdapter = new ServiceOnlyListAdapter(services,getActivity());
                            servicesList.setAdapter(serviceOnlyListAdapter);
                            getView().invalidate();
                        }
                        break;
                    case 401:
                        Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"user_not_found"), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (resp != null) {
                            Toast.makeText(getActivity(), "مشکلی از سمت سرور به وجود آمده است لطفا دوباره تلاش کنید...", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"server_problem"), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            @Override
            public void onFailure(Call<Map<String,Service>> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("error:",t.getMessage());
            }
        });*/
    }

}
