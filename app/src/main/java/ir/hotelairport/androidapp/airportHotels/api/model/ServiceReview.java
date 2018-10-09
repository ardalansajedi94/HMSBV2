package ir.hotelairport.androidapp.airportHotels.api.model;

public class ServiceReview {
    Service serviceId;
    int count;

    public ServiceReview() {
    }

    public Service getService() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
