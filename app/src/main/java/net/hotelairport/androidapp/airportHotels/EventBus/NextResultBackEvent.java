package net.hotelairport.androidapp.airportHotels.EventBus;

import com.google.gson.JsonObject;

import net.hotelairport.androidapp.airportHotels.api.model.RoomReview;


public class NextResultBackEvent {
    JsonObject room;
    RoomReview roomReview;

    public NextResultBackEvent(JsonObject room, RoomReview roomReview) {
        this.room = room;
        this.roomReview = roomReview;
    }

    public RoomReview getRoomReview() {

        return roomReview;
    }

    public void setRoomReview(RoomReview roomReview) {
        this.roomReview = roomReview;
    }



    public JsonObject getRoom() {
        return room;
    }

    public void setRoom(JsonObject room) {
        this.room = room;
    }
}
