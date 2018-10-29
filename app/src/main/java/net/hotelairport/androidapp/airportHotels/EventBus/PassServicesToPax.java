package net.hotelairport.androidapp.airportHotels.EventBus;

import com.google.gson.JsonArray;

import java.util.List;

import net.hotelairport.androidapp.airportHotels.api.model.Service;

public class PassServicesToPax {
    JsonArray services;
    List<Service> serviceList;
    List<Integer> count;

    public PassServicesToPax(JsonArray services, List<Service> serviceList, List<Integer> count) {
        this.services = services;
        this.serviceList = serviceList;
        this.count = count;
    }

    public JsonArray getServices() {
        return services;
    }

    public void setServices(JsonArray services) {
        this.services = services;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }
}
