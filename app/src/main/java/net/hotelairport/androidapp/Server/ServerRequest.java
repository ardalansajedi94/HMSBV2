package net.hotelairport.androidapp.Server;

/**
 * Created by Mohammad on 8/30/2017.
 */

public class ServerRequest {
    private String username,password,address,date,time,car,explanation,service,type,request_type,problems,title,content,place;
    private String firstname,lastname,national_id,phone,mobile,email,job,zip_code,_method,token,device_id,qr,api_token;
    private int count,food_id,item_type,device_type,notf_on,place_id,place_type;
    private double lat,lng;

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public void setPlace_type(int place_type) {
        this.place_type = place_type;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setNotf_on(int notf_on) {
        this.notf_on = notf_on;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }

    public void set_method(String _method) {
        this._method = _method;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public void setType(int type) {
        this.item_type = type;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setNational_id(String national_id) {
        this.national_id = national_id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setProblems(String problems) {
        this.problems = problems;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}
