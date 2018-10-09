package ir.hotelairport.androidapp.airportHotels.EventBus;

import ir.hotelairport.androidapp.airportHotels.api.model.RoomReview;

public class ReviewBackEvent {
    RoomReview roomReview;

    public ReviewBackEvent(RoomReview roomReview) {
        this.roomReview = roomReview;
    }

    public RoomReview getRoomReview() {
        return roomReview;
    }

    public void setRoomReview(RoomReview roomReview) {
        this.roomReview = roomReview;
    }
}
