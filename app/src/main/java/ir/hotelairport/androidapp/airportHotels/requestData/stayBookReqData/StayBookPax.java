package ir.hotelairport.androidapp.airportHotels.requestData.stayBookReqData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StayBookPax {


//            "fullName":"sdxsad",
//            "nationalCode":"sdxsad",
//            "ageGroup":1,
//            "email":"hamed@gmail.com",
//            "mobile":"09368888388 ",
//            "services":[
//                  {
//                      "serviceId":2,
//                      "count":1
//                  },
//                  {
//                      "serviceId":1,
//                      "Count":1
//                  }
//             ]

    private String fullName, nationalCode, ageGroup, email, mobile;
    private ArrayList<Map<String, Integer>> services;

    public StayBookPax(String ageGroup) {
        this.services = new ArrayList<>();
        this.ageGroup = ageGroup;
    }

    public void addService(int serviceId, int count) {

        Map<String, Integer> service = new HashMap<>();

        service.put("serviceId", serviceId);
        service.put("count", count);

        this.services.add(service);
    }

    public void removeService(int serviceId) {

        int index = -1;

        for (int i = 0; i < this.services.size(); i++) {
            if (this.services.get(i).get("serviceId") == serviceId) {
                index = i;
            }
        }

        if (index != -1) {
            this.services.remove(index);
        }

    }

    public void setServices(ArrayList<Map<String, Integer>> services) {
        this.services = services;
    }

    public ArrayList<Map<String, Integer>> getServices() {
        return this.services;
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

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
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

    public boolean validate() {

        if (fullName == null || fullName.equals("")) {
            return false;
        } else if (nationalCode == null || nationalCode.equals("")) {
            return false;
        } else if (mobile == null || mobile.equals("")) {
            return false;
        } else if (email == null || mobile.equals("")) {
            return false;
        }
        if (services == null) {
            return false;
        }

        return true;
    }
}
