package com.mohammadsharabati.restaurantgenieserver.Model;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String note;
    private String total;
    private String status;
    private List<Order> foods; // List of food order

    public Request() {
    }

    public Request(String phone, String name, String note, String total, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.note = note;
        this.total = total;
        this.foods = foods;
        this.status ="0"; // Default is 0, 0:Placed, 1:Shipping, 2:shipped
    }

    public String getStatus() {
        return status;
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

    public void setNote(String address) {
        this.note = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
