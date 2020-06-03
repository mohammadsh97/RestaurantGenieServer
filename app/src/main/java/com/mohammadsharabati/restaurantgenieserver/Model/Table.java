package com.mohammadsharabati.restaurantgenieserver.Model;

public class Table {

    private String name, phone, staffId;

    public Table() {
    }

    public Table(String name, String phone, String staffId) {
        this.name = name;
        this.phone = phone;
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}