package com.example.developer.ziptown.models;

public class Publisher {
    private  String name, contact;

    public Publisher(String name, String contact){
        this.name = name;
        this.contact = contact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public String getName() {
        return name;
    }
}
