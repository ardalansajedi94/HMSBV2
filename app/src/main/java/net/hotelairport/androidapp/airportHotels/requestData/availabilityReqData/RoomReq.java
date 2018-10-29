package net.hotelairport.androidapp.airportHotels.requestData.availabilityReqData;

import org.json.JSONException;
import org.json.JSONObject;

public class RoomReq {

    private int adults;
    private int children;

    public RoomReq(int adultCount, int childCount){

        this.adults = adultCount;
        this.children = childCount;

    }

    public JSONObject getData() {

        JSONObject data = new JSONObject();


        try {
            data.put("Adults", this.adults);
            data.put("childs", this.children);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }
}
