package net.hotelairport.androidapp.airportHotels.requestData;

import java.util.ArrayList;

import net.hotelairport.androidapp.airportHotels.requestData.stayBookReqData.StayBookRoom;

public class StayBookReq {

//    "refId":"1531244996OXYVs9k3ZujQ16PAxUP0",
//            "reservedId":15,
//            "room":[]

    private String refId;
    private int reservedId;
    private ArrayList<StayBookRoom> rooms;

    public StayBookReq() {
        this.rooms = new ArrayList<>();
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public int getReservedId() {
        return reservedId;
    }

    public void setReservedId(int reservedId) {
        this.reservedId = reservedId;
    }

    public ArrayList<StayBookRoom> getRooms() {
        return rooms;
    }

    public void addRoom(StayBookRoom room) {
        this.rooms.add(room);
    }

    public void setRooms(ArrayList<StayBookRoom> rooms) {
        this.rooms = rooms;
    }
}
