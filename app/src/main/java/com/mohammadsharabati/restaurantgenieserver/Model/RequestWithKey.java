package com.mohammadsharabati.restaurantgenieserver.Model;

import java.util.List;

public class RequestWithKey {

    private String phone;
    private String name;
    private String note;
    private String total;
    private String status;
    private List<Order> foods; // List of food order
    private String key;


    public RequestWithKey(String phone, String name, String note, String total, String status, List<Order> foods, String key) {
        this.phone = phone;
        this.name = name;
        this.note = note;
        this.total = total;
        this.status = status;
        this.foods = foods;
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
