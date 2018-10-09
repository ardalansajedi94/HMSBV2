package ir.hotelairport.androidapp.airportHotels.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomReview {
    String roomId;
    int adults;
    int childs;
    @SerializedName("")
    List<PaxReview> paxReviews;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }

    public List<PaxReview> getPaxReviews() {
        return paxReviews;
    }

    public void setPaxReviews(List<PaxReview> paxReviews) {
        this.paxReviews = paxReviews;
    }

    public RoomReview() {
    }
}
