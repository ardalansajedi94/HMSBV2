package ir.hotelairport.androidapp.airportHotels.requestData.stayBookReqData;

import java.util.ArrayList;

public class StayBookRoom {

//            "roomId":6,
//            "adults":1,
//            "childs":1,
//            "pax":[]

    private int roomId, adults, children;
    private ArrayList<StayBookPax> paxes;

    public StayBookRoom(int roomId, int adult, int child) {
        this.paxes = new ArrayList<>();

        this.roomId = roomId;
        this.adults = adult;
        this.children = child;

    }

    public int getRoomId() {
        return roomId;
    }


    public int getAdults() {
        return adults;
    }


    public int getChildren() {
        return children;
    }


    public ArrayList<StayBookPax> getPaxes() {
        return paxes;
    }

    public void addPax(StayBookPax pax) {
        this.paxes.add(pax);
    }

    public void setPaxes(ArrayList<StayBookPax> paxes) {
        this.paxes = paxes;
    }
}
