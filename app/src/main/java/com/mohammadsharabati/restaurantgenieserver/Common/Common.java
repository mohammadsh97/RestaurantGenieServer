package com.mohammadsharabati.restaurantgenieserver.Common;

import com.mohammadsharabati.restaurantgenieserver.Model.Request;
import com.mohammadsharabati.restaurantgenieserver.Model.User;

public class Common {
    public static User currentUser;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    // Request to upload image
    public static final int PICK_IMAGE_REQUEST = 71;

    public static Request currentRequest;

    public static String convertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Placed";
            case "1":
                return "Processing";
            default:
                return "Ready, On the way to you";
        }
    }

}