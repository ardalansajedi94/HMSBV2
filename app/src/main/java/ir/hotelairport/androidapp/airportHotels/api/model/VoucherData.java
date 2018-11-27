package ir.hotelairport.androidapp.airportHotels.api.model;

public class VoucherData {
    String checkIn, checkOut;
    int type, total_price;

    public VoucherData() {
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTotalPrice() {
        return total_price;
    }

    public void setTotalPrice(int totalPrice) {
        this.total_price = totalPrice;
    }
}
