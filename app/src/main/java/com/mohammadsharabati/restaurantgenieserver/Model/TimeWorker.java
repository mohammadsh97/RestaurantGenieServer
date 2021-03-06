package com.mohammadsharabati.restaurantgenieserver.Model;

public class TimeWorker {

    private String name , startTime , endTime ,dayId;

    public TimeWorker() {

    }

    public TimeWorker(String name, String startTime, String endTime, String dayId) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayId = dayId;
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

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }
}
