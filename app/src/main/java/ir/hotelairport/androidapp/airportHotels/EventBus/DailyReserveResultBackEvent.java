package ir.hotelairport.androidapp.airportHotels.EventBus;

import ir.hotelairport.androidapp.airportHotels.api.model.Room;

public class DailyReserveResultBackEvent {
    boolean clicked;
    int[] counter;
    Room[] confirm;

    public DailyReserveResultBackEvent(boolean clicked, int[] counter, Room[] confirm) {
        this.clicked = clicked;
        this.counter = counter;
        this.confirm = confirm;
    }

    public int[] getCounter() {
        return counter;
    }

    public void setCounter(int[] counter) {
        this.counter = counter;
    }

    public Room[] getConfirm() {
        return confirm;
    }

    public void setConfirm(Room[] confirm) {
        this.confirm = confirm;
    }
}
