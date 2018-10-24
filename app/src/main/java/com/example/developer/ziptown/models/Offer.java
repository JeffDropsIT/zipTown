package com.example.developer.ziptown.models;

import java.io.Serializable;

public class Offer implements Serializable{
    private String publisher, contact, created, days, time, city, destination, origin;
    private Publisher publisherObj;


    public Offer(String origin, String destination, String time, String days, String city, String created, Publisher publisher){
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.days = days;
        this.city = city;
        this.created = created;
        this.publisher = publisher.getName();
        this.contact = publisher.getContact();
        this.publisherObj = publisher;
    }


    public String getContact() {
        return contact;
    }

    public String getCity() {
        return city;
    }

    public String getCreated() {
        return created;
    }

    public String getDays() {
        return days;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getPublisher() {
        return publisher;
    }
    public int getPublisherId(){return publisherObj.getId();}

    public String getTime() {
        return time;
    }


    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
