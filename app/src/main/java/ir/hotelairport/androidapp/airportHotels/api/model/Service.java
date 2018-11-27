package ir.hotelairport.androidapp.airportHotels.api.model;

public class Service {

        String title_fa;
        int start_access,
            end_access;
        double priceByDiscount;
        double price;
        int service_id;
        int hotel_id;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Service() {
    }

    public String getTitle_fa() {
        return title_fa;
    }

    public void setTitle_fa(String title_fa) {
        this.title_fa = title_fa;
    }

    public int getStart_access() {
        return start_access;
    }

    public void setStart_access(int start_access) {
        this.start_access = start_access;
    }

    public int getEnd_access() {
        return end_access;
    }

    public void setEnd_access(int end_access) {
        this.end_access = end_access;
    }

    public double getPriceByDiscount() {
        return priceByDiscount;
    }

    public void setPriceByDiscount(double priceByDiscount) {
        this.priceByDiscount = priceByDiscount;
    }


}
