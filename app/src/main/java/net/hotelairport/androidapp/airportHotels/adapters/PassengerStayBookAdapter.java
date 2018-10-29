package net.hotelairport.androidapp.airportHotels.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hotelairport.androidapp.airportHotels.EventBus.NextResultBackEvent;
import net.hotelairport.androidapp.airportHotels.PreferenceManager.MyPreferenceManager;
import net.hotelairport.androidapp.R;
import net.hotelairport.androidapp.airportHotels.api.model.PaxReview;
import net.hotelairport.androidapp.airportHotels.api.model.RoomReview;
import net.hotelairport.androidapp.airportHotels.api.model.Service;
import net.hotelairport.androidapp.airportHotels.api.model.ServiceReview;


public class PassengerStayBookAdapter extends RecyclerView.Adapter<PassengerStayBookAdapter.ViewHolder>{

    private Context context;
    private int index = 0;
    private ArrayList<String> nameString=new ArrayList<>() ,idString=new ArrayList<>() , mobileString=new ArrayList<>() , emailString=new ArrayList<>()  ;
    private boolean[] flag={true , true},persianName = {true , true} , englishName ={false , false} , iranId={true , true}, foreignId ={false , false} ,validation= {true , true};
    private ArrayList<ArrayList<Service>> services = new ArrayList<>();
    private View view;
    ViewHolder hold;


    public PassengerStayBookAdapter(Context context , int position  ) {
        this.context = context;
        index= position;
        EventBus.getDefault().register(this);

    }
    @Subscribe
    public void onNextClicked(JsonObject room){
        int save= MyPreferenceManager.getInstace(context).getPosition();


        if ( save== index){
            if (isValidation()){
                RoomReview roomReview = new RoomReview();
                JsonObject obj = new JsonObject();
                obj.addProperty("roomId", MyPreferenceManager.getInstace(context).getRoom().get( index ).getRoom_id());
                roomReview.setRoomId(String .valueOf( MyPreferenceManager.getInstace(context).getRoom().get( index ).getRoom_id() ));
                obj.addProperty("adults", MyPreferenceManager.getInstace(context).getRoom().get( index ).getAdults());
                roomReview.setAdults(MyPreferenceManager.getInstace(context).getRoom().get( index ).getAdults());
                obj.addProperty("childs", 0);
                roomReview.setChilds(0);
                JsonArray pax = new JsonArray();
                List<PaxReview> paxReviewList = new ArrayList<>();
                for (int i = 0 ; i<nameString.size() ; i++) {
                    PaxReview paxReview = new PaxReview();

                    JsonObject object = new JsonObject();
                    object.addProperty("fullName", nameString.get(i));
                    paxReview.setFullName(nameString.get(i));
                    object.addProperty("nationalCode", idString.get(i));
                    paxReview.setNationalCode(idString.get(i));
                    object.addProperty("ageGroup", 1);
                    paxReview.setAgeGroup(1);
                    paxReview.setDocType(flag[i]);
                    object.addProperty("email", emailString.get(i));
                    paxReview.setEmail(emailString.get(i));
                    object.addProperty("mobile", mobileString.get(i));
                    paxReview.setMobile(mobileString.get(i));
                    JsonArray service = new JsonArray();
                    List<ServiceReview> serviceReviewList = new ArrayList<>();
                    if (services.size()!=0)
                        for (int j=0 ; j<services.get(i).size(); j++) {
                            JsonObject objct = new JsonObject();
                            ServiceReview serviceReview = new ServiceReview();
                            if (services.get(i)!=null) {
                                objct.addProperty("serviceId", services.get(i).get(j).getService_id());
                                serviceReview.setServiceId(services.get(i).get(j));
                                objct.addProperty("count", 1);
                                serviceReview.setCount(1);
                                service.add(objct);
                                serviceReviewList.add(serviceReview);
                            }
                        }
                    paxReview.setServiceReviews(serviceReviewList);

                    object.add("prices", service);
                    paxReviewList.add(paxReview);
                    pax.add(object);
                }
                obj.add("pax" , pax);
                roomReview.setPaxReviews(paxReviewList);
                EventBus.getDefault().post(new NextResultBackEvent(obj , roomReview));
                EventBus.getDefault().unregister(this);

            }
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.passenger_detail , parent , false);
        return new ViewHolder(view);

    }


    public void showTextViewsAsMandatory ( TextView... tvs )
    {


        for ( TextView tv : tvs )
        {
            String text = tv.getText ().toString ();

            tv.setText ( Html.fromHtml ( text + "<font color=\"#ff0000\">" + " * " + "</font>" ) );
        }
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        hold = holder;
        int servicesSize = MyPreferenceManager.getInstace(context).getRoom().get( index ).getServices().size();
        ArrayList<Service> services= MyPreferenceManager.getInstace(context).getRoom().get( index ).getServices();
        servicesSize-=2;
        ServiceStayBookAdapter serviceListAdapter = new ServiceStayBookAdapter(context , servicesSize , services ,myCheckedService , position );
        final LinearLayoutManager layout = new LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false);
        layout.setAutoMeasureEnabled(true);
        holder.recyclerView.setLayoutManager(layout );
        holder.recyclerView.setAdapter(serviceListAdapter);
        getEditTextValue(holder ,position);
        if (position == 0){
            holder.passenger.setText( "اطلاعات مسافر اول" );
        }
        else {
            holder.passenger.setText( "اطلاعات مسافر دوم" );
        }

        holder.iran.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.name.setHint("نام به فارسی");
                holder.id.setInputType(2);
                holder.id.setHint("کد ملی");
                holder.iran.setTextColor( Color.parseColor( "#ffffff"));
                TransitionDrawable transition = (TransitionDrawable) holder.foriegn.getBackground();
                transition.reverseTransition(200);
                TransitionDrawable transitionIran = (TransitionDrawable) holder.iran.getBackground();
                transitionIran.reverseTransition(200);
                holder.foriegn.setTextColor(Color.parseColor( "#bbbbbb" ));
                iranId[position]=true;
                flag[position]= true;
                persianName[position]=true;
                englishName[position] =false;
                foreignId[position] =false;
            }
        } );
        if (position == 0){
            holder.passenger.setText( "اطلاعات مسافر اول" );
        }
        else {
            holder.passenger.setText( "اطلاعات مسافر دوم" );
        }
        holder.foriegn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.name.setHint("نام به انگلیسی");
                foreignId[position] =true;
                englishName[position] =true;
                flag[position]= false;
                holder.id.setInputType(131073);
                holder.id.setHint("شماره پاسپورت");
                iranId[position]=false;
                persianName[position]=false;
                TransitionDrawable transition = (TransitionDrawable) holder.foriegn.getBackground();
                transition.reverseTransition(200);
                TransitionDrawable transitionIran = (TransitionDrawable) holder.iran.getBackground();
                transitionIran.reverseTransition(200);
                holder.foriegn.setTextColor( Color.parseColor( "#ffffff" )  );
                holder.iran.setTextColor(Color.parseColor( "#bbbbbb") );
            }
        } );

    }


    @Override
    public int getItemCount() {
        int size=MyPreferenceManager.getInstace(context).getRoom().get( index ).getAdults();
        return size;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        EditText name;
        EditText id;
        EditText mobile;
        EditText email;
        TextView passenger;
        RecyclerView recyclerView;
        Button iran , foriegn;
        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.edit_name);
            id = itemView.findViewById(R.id.edit_nat_code);
            mobile = itemView.findViewById(R.id.edit_cell_num);
            email = itemView.findViewById(R.id.edit_email);
            recyclerView = itemView.findViewById(R.id.service_list);
            passenger = itemView.findViewById(R.id.passenger);
            iran = itemView.findViewById(R.id.iran);
            foriegn = itemView.findViewById(R.id.foreign);

        }
    }

    public void getEditTextValue(final ViewHolder holder , final int position){
        holder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.name.setBackgroundResource(R.drawable.editor_valid);
                try {
                    nameString.remove(position);
                    nameString.add(position, s.toString());
                }
                catch (IndexOutOfBoundsException e)
                {
                    nameString.add(position,s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        holder.id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.id.setBackgroundResource(R.drawable.editor_valid);
                try {
                    idString.remove(position);
                    idString.add(position,s.toString());
                }
                catch (IndexOutOfBoundsException e)
                {
                    idString.add(position,s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        holder.mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.mobile.setBackgroundResource(R.drawable.editor_valid);
                try {
                    mobileString.remove(position);
                    mobileString.add(position,s.toString());
                }
                catch (IndexOutOfBoundsException e)
                {
                    mobileString.add(position,s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        holder.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.email.setBackgroundResource(R.drawable.editor_valid);
                try{
                    emailString.remove(position);
                    emailString.add(position,s.toString());
                }
                catch (IndexOutOfBoundsException e)
                {
                    emailString.add(position,s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    public static boolean textPersian(String s) {
        for (int i = 0; i < Character.codePointCount(s, 0, s.length()); i++) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <=0x06FF || c== 0xFB8A || c==0x067E || c==0x0686 || c==0x06AF)
                return true;
        }
        return false;}

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidNatId(String natId){
        if (natId.equals("0000000000"))
            return false;
        else if (natId.equals("1111111111"))
            return false;
        else if (natId.equals("2222222222"))
            return false;
        else if (natId.equals("3333333333"))
            return false;
        else if (natId.equals("4444444444"))
            return false;
        else if (natId.equals("5555555555"))
            return false;
        else if (natId.equals("6666666666"))
            return false;
        else if (natId.equals("7777777777"))
            return false;
        else if (natId.equals("8888888888"))
            return false;
        else if (natId.equals("9999999999"))
            return false;
        else if (natId.equals("0123456789"))
            return false;
        else if (natId.equals("9876543210"))
            return false;
        else if (natId.equals("9999999999"))
            return false;
        else if (natId.length()!=10){
            return false;

        }
        else
            return true;
    }

    public boolean mobileValidation(String mobile){

        Pattern pattern = Pattern.compile("^[0][9][1][0-9]{8,8}$");
        Pattern pattern1 = Pattern.compile("^[0][9][0][0-9]{8,8}$");
        Pattern pattern2 = Pattern.compile("^[0][9][2][0-9]{8,8}$");
        Pattern pattern3 = Pattern.compile("^[0][9][3][0-9]{8,8}$");
        Pattern pattern4 = Pattern.compile("^[0][9][4][0-9]{8,8}$");
        Matcher matcher = pattern.matcher(mobile);
        Matcher matcher1 = pattern1.matcher(mobile);
        Matcher matcher2 = pattern2.matcher(mobile);
        Matcher matcher3 = pattern3.matcher(mobile);
        Matcher matcher4 = pattern4.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }
        else if (matcher1.matches()){
            return true;
        }
        else if (matcher2.matches()){
            return true;
        }
        else if (matcher3.matches()){
            return true;
        }
        else if (matcher4.matches()){
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isValidation() {
        if(nameString.size()!=0){
            for (int i = 0 ; i <nameString.size() ; i++) {
                if (nameString.get(i) == null) {
                    Toast.makeText(context, "نام نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                    hold.name.setBackgroundResource(R.drawable.editor);
                    return false;
                } else {
                    if (persianName[i]) {
                        if (!textPersian(nameString.get(i))) {
                            Toast.makeText(context, "نام را به فارسی وارد کنید", Toast.LENGTH_LONG).show();
                            hold.name.setBackgroundResource(R.drawable.editor);
                            return false;
                        }
                    } else if (englishName[i]) {
                        if (textPersian(nameString.get(i))) {
                            Toast.makeText(context, "نام را به انگلیسی وارد کنید", Toast.LENGTH_LONG).show();
                            hold.name.setBackgroundResource(R.drawable.editor);
                            return false;
                        }
                    }
                }
                if (iranId[i]) {
                    if (idString.get(i) == null) {
                        Toast.makeText(context, "کد ملی نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                        hold.id.setBackgroundResource(R.drawable.editor);
                        return false;
                    }
                    if (!isValidNatId(idString.get(i))) {
                        Toast.makeText(context, "کد ملی صحیح وارد نشده است", Toast.LENGTH_LONG).show();
                        hold.id.setBackgroundResource(R.drawable.editor);
                        return false;
                    }
                } else if (foreignId[i]) {
                    if (idString.get(i) == null) {
                        Toast.makeText(context, "شماره پاسپورت نمی تواند خالی باشد", Toast.LENGTH_LONG).show();
                        hold.id.setBackgroundResource(R.drawable.editor);
                        return false;
                    }
                }
                if (!mobileValidation(mobileString.get(i))){
                    Toast.makeText(context, "موبایل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
                    hold.mobile.setBackgroundResource(R.drawable.editor);
                    return false;
                }
                if (!isValidEmail(emailString.get(i))) {
                    Toast.makeText(context, "ایمیل صحیح وارد نشده است", Toast.LENGTH_LONG).show();
                    hold.email.setBackgroundResource(R.drawable.editor);
                    return false;
                }


            }
            return true;}
            Toast.makeText( context , "اطلاعات صحیح وارد نشده است"  , Toast.LENGTH_LONG).show();
        return false;}

    ServiceStayBookAdapter.myCheckedService myCheckedService = new ServiceStayBookAdapter.myCheckedService(){
        @Override
        public void onItemCheck(ArrayList<Service> serviceId , int position) {
            if (services.size()==position){
                services.add( position , serviceId);
            }
            else
            {
                services.remove(position);
                services.add(position , serviceId);
            }

        }
    };
}