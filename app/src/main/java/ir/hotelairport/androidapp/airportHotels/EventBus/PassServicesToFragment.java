package ir.hotelairport.androidapp.airportHotels.EventBus;

import java.util.List;

import ir.hotelairport.androidapp.airportHotels.api.model.Service;

public class PassServicesToFragment {
    List<Service> serviceList;
    List<Integer> count;

    public PassServicesToFragment(List<Service> serviceList, List<Integer> count) {

        this.serviceList = serviceList;
        this.count = count;
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
