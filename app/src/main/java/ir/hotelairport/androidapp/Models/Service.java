package ir.hotelairport.androidapp.Models;

public class Service {
    int price;
    int price_with_discount;
    int code;
    int count;
    String name;
    String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice_with_discount() {
        return price_with_discount;
    }

    public void setPrice_with_discount(int price_with_discount) {
        this.price_with_discount = price_with_discount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
