package net.hotelairport.androidapp.airportHotels.api.model;

import java.util.ArrayList;

public class ServicesResponse {
    String ref_id;
    ArrayList<Service> services = new ArrayList<>();

    public ServicesResponse() {
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}
