package com.mohammadsharabati.restaurantgenieserver.Common;

import com.mohammadsharabati.restaurantgenieserver.Model.User;

public class Common {
    public static User currentUser;

    public static final String DELETE = "Delete";


    public static String convertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "On my way";
            default:
                return "Shipped";
        }
    }
}