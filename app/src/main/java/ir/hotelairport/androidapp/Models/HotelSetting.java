package ir.hotelairport.androidapp.Models;

/**
 * Created by Mohammad on 10/26/2017.
 */

public class HotelSetting {
    private int language_id;
    private String hotel_name, hotel_logo;

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_logo() {
        return hotel_logo;
    }

    public void setHotel_logo(String hotel_logo) {
        this.hotel_logo = hotel_logo;
    }
}
