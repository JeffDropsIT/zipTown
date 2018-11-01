package com.example.developer.ziptown.models.forms;

import com.example.developer.ziptown.models.baseClasses.BasePost;

public class CreateCustomSearch extends BasePost {
    public CreateCustomSearch(String city,
                       String returnTime, String destination, String days, String origin, String depatureTime ){
        super(city, returnTime, destination, days, origin, depatureTime);
    }

    @Override
    public String getReturnTime() {
        return super.getReturnTime();
    }

    @Override
    public String getDestination() {
        return super.getDestination();
    }

    @Override
    public String getDepatureTime() {
        return super.getDepatureTime();
    }

    @Override
    public String getDays() {
        return super.getDays();
    }
    @Override
    public String getOrigin() {
        return super.getOrigin();
    }

    @Override
    public String getCity() {
        return super.getCity();
    }

    @Override
    public void setDestination(String destination) {
        super.setDestination(destination);
    }

    @Override
    public void setReturnTime(String returnTime) {
        super.setReturnTime(returnTime);
    }

    @Override
    public void setDepatureTime(String depatureTime) {
        super.setDepatureTime(depatureTime);
    }

    @Override
    public void setOrigin(String origin) {
        super.setOrigin(origin);
    }

    @Override
    public void setCity(String city) {
        super.setCity(city);
    }

    @Override
    public void setDays(String days) {
        super.setDays(days);
    }
}
