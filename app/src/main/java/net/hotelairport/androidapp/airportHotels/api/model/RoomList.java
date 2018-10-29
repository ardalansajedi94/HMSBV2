package net.hotelairport.androidapp.airportHotels.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoomList {
    @SerializedName("")
    List<RoomReview> roomReviews;

    public List<RoomReview> getRoomReviews() {
        return roomReviews;
    }

    public void setRoomReviews(List<RoomReview> roomReviews) {
        this.roomReviews = roomReviews;
    }

    public RoomList() {
    }
}
