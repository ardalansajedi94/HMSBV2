package net.hotelairport.androidapp.airportHotels.api.model;

import java.util.ArrayList;
import java.util.List;

public class Room{


            int hotel_id,
                adults,
                room_id,
                star;

            String hotel_title_en,
                   room_title_fa;

    List<Price> price;

            ArrayList<Service> services = new ArrayList<>();

    public Room() {
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getHotel_title_en() {
        return hotel_title_en;
    }

    public void setHotel_title_en(String hotel_title_en) {
        this.hotel_title_en = hotel_title_en;
    }

    public String getRoom_title_fa() {
        return room_title_fa;
    }

    public void setRoom_title_fa(String room_title_fa) {
        this.room_title_fa = room_title_fa;
    }

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }


    public ArrayList<Service> getServices() {
        return  services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}
