package ir.hotelairport.androidapp.airportHotels.responseData;

import java.util.ArrayList;

public class ReserveRes {

    /*
    {
        "refId": "15316661910hFlKWPcwmSGGCCYZ6nU",
            "reservedRoomsDetail": [
        "3",
                "3"
        ],
        "reservedId": 267,
            "expireDate": 2400
    }
    */

    private String refId;

    private ArrayList<String> reservedRoomsDetail = new ArrayList<>();

    private int reservedId;

    private int expireDate;

    public ReserveRes() {
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public void setReservedRoomsDetail(ArrayList<String> reservedRoomsDetail) {
        this.reservedRoomsDetail = reservedRoomsDetail;
    }

    public void setReservedId(int reservedId) {
        this.reservedId = reservedId;
    }

    public void setExpireDate(int expireDate) {
        this.expireDate = expireDate;
    }

    public String getRefId() {
        return refId;
    }

    public ArrayList<String> getReservedRoomsDetail() {
        return reservedRoomsDetail;
    }

    public int getReservedId() {
        return reservedId;
    }

    public int getExpireDate() {
        return expireDate;
    }


}
