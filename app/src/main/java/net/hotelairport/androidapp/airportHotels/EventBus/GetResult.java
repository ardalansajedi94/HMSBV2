package net.hotelairport.androidapp.airportHotels.EventBus;

import net.hotelairport.androidapp.airportHotels.api.model.AvailabilityRes;

public class GetResult {
    public AvailabilityRes getRes() {
        return res;
    }

    public void setRes(AvailabilityRes res) {
        this.res = res;
    }

    AvailabilityRes res ;

    public GetResult(AvailabilityRes res) {
        this.res = res;
    }
}
