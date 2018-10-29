package net.hotelairport.androidapp.airportHotels.api.model;

public class Price {
    String month;
    int priceByMonth, nights;

    public Price() {
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPriceByMonth() {
        return priceByMonth;
    }

    public void setPriceByMonth(int priceByMonth) {
        this.priceByMonth = priceByMonth;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }
}
