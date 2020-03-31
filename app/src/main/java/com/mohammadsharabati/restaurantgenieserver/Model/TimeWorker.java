package com.mohammadsharabati.restaurantgenieserver.Model;

public class TimeWorker {

    private String name , startTime , endTime;

    public TimeWorker() {

    }

    public TimeWorker(String name, String startTime, String endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

//    public int getLength() {
//        if (Integer.parseInt(endTime) != 0 && Integer.parseInt(startTime) != 0)
//            return Integer.parseInt(endTime) - Integer.parseInt(startTime);
//
//        return 0;
//    }
}
