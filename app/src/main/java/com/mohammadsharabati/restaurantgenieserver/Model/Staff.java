package com.mohammadsharabati.restaurantgenieserver.Model;

import java.util.ArrayList;

public class Staff {

    private String businessNumber, email, Phone, Name, Password;

    private ArrayList<User> tableList;

    public Staff(String businessNumber, String email, String phone, String name, String password, ArrayList<User> tableList) {
        this.businessNumber = businessNumber;
        this.email = email;
        Phone = phone;
        Name = name;
        Password = password;
        this.tableList = tableList;
    }

    public Staff() {
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public ArrayList<User> getTableList() {
        return tableList;
    }

    public void setTableList(ArrayList<User> tableList) {
        this.tableList = tableList;
    }
}
