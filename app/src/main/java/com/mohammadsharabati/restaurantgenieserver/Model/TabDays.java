package com.mohammadsharabati.restaurantgenieserver.Model;

import androidx.fragment.app.Fragment;

public class TabDays {
    private String name;
    private Fragment fragment;

    public TabDays() {
    }

    public TabDays(String name, Fragment fragment) {
        this.name = name;
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
