package com.mohammadsharabati.restaurantgenieserver.Model;

public class Category {
    private String Name ;
    private String Image ;

    public Category() {
    }

    public Category(String name, String image) {
        Image = image;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}