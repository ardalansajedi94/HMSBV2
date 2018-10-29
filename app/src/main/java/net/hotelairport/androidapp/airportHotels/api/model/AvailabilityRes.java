package net.hotelairport.androidapp.airportHotels.api.model;

import java.util.ArrayList;



public class AvailabilityRes {

    String refId;
    ArrayList<Room> rooms = new ArrayList<>();

    public AvailabilityRes() {
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
