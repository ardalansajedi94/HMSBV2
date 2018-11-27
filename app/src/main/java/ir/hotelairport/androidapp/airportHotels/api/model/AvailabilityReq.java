package ir.hotelairport.androidapp.airportHotels.api.model;


public class AvailabilityReq {


    String api_token, checkIn, checkOut;


    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }


    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }


    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }


    public AvailabilityReq() {

    }
}
