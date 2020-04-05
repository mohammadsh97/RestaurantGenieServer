package com.mohammadsharabati.restaurantgenieserver.Model;

public class Staff {

    private String email, Phone, Name, Password;

    public Staff() {
    }

    public Staff(String email, String phone, String name, String password) {
        this.email = email;
        Phone = phone;
        Name = name;
        Password = password;
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
}
