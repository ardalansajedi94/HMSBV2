package net.hotelairport.androidapp.airportHotels.requestData;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReserveReq implements Parcelable {

    /*
        {
            "refId": "1531566902YPOnlvmuVJL7lxqMXerS",
                "ip":"0",
                "rooms": [
                    {
                        "roomId": "3",
                            "count": "2"
                    }
              ]
        }
    */

    private String refId;
    private String ip;
    private ArrayList<Map<String, String>> rooms;

    public ReserveReq(String refId){
        this.ip = "0";
        this.refId = refId;
    }

    protected ReserveReq(Parcel in) {
        refId = in.readString();
        ip = in.readString();
    }

    public static final Creator<ReserveReq> CREATOR = new Creator<ReserveReq>() {
        @Override
        public ReserveReq createFromParcel(Parcel in) {
            return new ReserveReq(in);
        }

        @Override
        public ReserveReq[] newArray(int size) {
            return new ReserveReq[size];
        }
    };

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void addRoom(String roomId, String count) {

        Map<String, String> room = new HashMap<>();

        room.put("roomId", roomId);
        room.put("count", count);

        this.rooms.add(room);
    }

    public void setRooms(ArrayList<Map<String, String>> rooms) {
        this.rooms = rooms;
    }

    public JSONObject getData() throws JSONException {

        JSONObject data = new JSONObject();

        data.put("refId", this.refId);
        data.put("ip", this.ip);

        JSONArray rm = new JSONArray(this.rooms);

        data.put("rooms", rm);

        return data;
    }

    public JSONObject getPlaceHolderData() throws JSONException {

        JSONObject data = new JSONObject();

        data.put("refId", this.refId);
        data.put("ip", this.ip);



        JSONArray rm = new JSONArray(this.rooms);

        data.put("rooms", rm);

        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(refId);
        parcel.writeString(ip);
    }
}
