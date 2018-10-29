package net.hotelairport.androidapp.airportHotels.EventBus;

import com.google.gson.JsonArray;

public class getServices {
    JsonArray services = new JsonArray();

    public JsonArray getServices() {
        return services;
    }

    public void setServices(JsonArray services) {
        this.services = services;
    }

    public getServices(JsonArray services) {
        this.services = services;
    }
}
