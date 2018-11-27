package ir.hotelairport.androidapp.airportHotels.requestData;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

import ir.hotelairport.androidapp.airportHotels.api.model.Service;

public class ServiceBookReq implements Parcelable {

//    {
//        "refId": "1532423252s3Wd7zK7b5g5tYZXNYcE",
//        "checkIn": "2018-07-25 12:40:41",
//        "passenger":{
//            "nationalCode":"0013332122",
//            "fullName":"asdads",
//            "ageGroup":"1"
//        },
//        "services":[
//            {
//                "serviceId": "6",
//                "amount": 3
//            },
//            {
//                "serviceId": "7",
//                 "amount": 2
//            }
//        ]
//    }

    private String refId, checkInDate, checkInTime, nationalCode, fullName, ageGroup,
            phone, email;

    private ArrayList<Service> services = new ArrayList<>();

    public ServiceBookReq(String refId) {
        this.refId = refId;
        this.ageGroup = "1";
    }

    protected ServiceBookReq(Parcel in) {
        refId = in.readString();
        checkInDate = in.readString();
        checkInTime = in.readString();
        nationalCode = in.readString();
        fullName = in.readString();
        ageGroup = in.readString();
    }

    public static final Creator<ServiceBookReq> CREATOR = new Creator<ServiceBookReq>() {
        @Override
        public ServiceBookReq createFromParcel(Parcel in) {
            return new ServiceBookReq(in);
        }

        @Override
        public ServiceBookReq[] newArray(int size) {
            return new ServiceBookReq[size];
        }
    };

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRefId(String refId) {
        this.refId = refId;

    }

    public void setCheckInDate(String checkIn) {
        this.checkInDate = checkIn;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setServices(ArrayList<Service> services) {

        ArrayList<Service> servicesFiltered = new ArrayList<>();

        for (Service service : services) {
//            if(service.getCount() > 0) {
//                servicesFiltered.add(service);
//            }
        }

        this.services = servicesFiltered;
    }

    public String getRefId() {
        return refId;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAgeGroup() {

        return ageGroup;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public JSONObject getData() {
        JSONObject jsObj = new JSONObject();


        return jsObj;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(refId);
        dest.writeString(checkInDate);
        dest.writeString(checkInTime);
        dest.writeString(nationalCode);
        dest.writeString(fullName);
        dest.writeString(ageGroup);
    }
}
