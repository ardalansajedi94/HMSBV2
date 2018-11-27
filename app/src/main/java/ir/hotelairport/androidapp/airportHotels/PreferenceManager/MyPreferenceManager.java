package ir.hotelairport.androidapp.airportHotels.PreferenceManager;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ir.hotelairport.androidapp.airportHotels.api.model.LoginRes;
import ir.hotelairport.androidapp.airportHotels.api.model.Room;


public class MyPreferenceManager {

    private static MyPreferenceManager instance = null;
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;
    public static MyPreferenceManager getInstace(Context context){
        if (instance ==null){
            instance= new MyPreferenceManager(context);
        }
        return instance;
    }
    private MyPreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences("My_preference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public String getToken(){
        return sharedPreferences.getString("token" , null);
    }
    public void putToken(String token){
        editor.putString("token" , token);
        editor.apply();
    }
    public int getBookId(){
        return sharedPreferences.getInt("BookId" , 0);
    }
    public void putBookId(int bookId){
        editor.putInt("BookId" , bookId);
        editor.apply();
    }
    public String getCheckIn(){
        return sharedPreferences.getString("checkIn" , null);
    }
    public void putCheckIn(String checkIn){
        editor.putString("checkIn" , checkIn);
        editor.apply();
    }
    public int getPosition(){return sharedPreferences.getInt("position" , 0);}
    public void putPosition(int position){
        editor.putInt("position" ,position);
        editor.apply();
    }
    public String getRefId(){
        return sharedPreferences.getString("refId" , null);
    }
    public void putRefId(String refId){
        editor.putString("refId" , refId);
        editor.apply();
    }
    public ArrayList<Room> getRoom(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("room", null);
        Type type = new TypeToken<ArrayList<Room>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void putRoom(ArrayList<Room> room){
        Gson gson = new Gson();
        String json = gson.toJson(room);
        editor.putString("room", json);
        editor.apply();
    }


    public int getReservedId(){
        return sharedPreferences.getInt("reservedId" , 0);
    }
    public void putReservedId(int reservedId){
        editor.putInt("reservedId" , reservedId);
        editor.apply();
    }

    public LoginRes getLoginRes(){
        String loginRes = sharedPreferences.getString("LoginRes" , null);
        Gson gson = new Gson();
        return gson.fromJson(loginRes , LoginRes.class);
    }
    public void putLoginRes(LoginRes loginRes){
        Gson gson = new Gson();
        String loginResStr = gson.toJson(loginRes , LoginRes.class);
        editor.putString("LoginRes" ,loginResStr);
        editor.apply();
    }


}

