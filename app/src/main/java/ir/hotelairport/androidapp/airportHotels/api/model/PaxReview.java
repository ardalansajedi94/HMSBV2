package ir.hotelairport.androidapp.airportHotels.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaxReview {
    String fullName;
    String nationalCode;
    int ageGroup;
    boolean docType;
    String email;
    String mobile;
    @SerializedName("")
    List<ServiceReview> serviceReviews;

    public PaxReview() {
    }

    public boolean isDocType() {
        return docType;
    }

    public void setDocType(boolean docType) {
        this.docType = docType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(int ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ServiceReview> getServiceReviews() {
        return serviceReviews;
    }

    public void setServiceReviews(List<ServiceReview> serviceReviews) {
        this.serviceReviews = serviceReviews;
    }
}
