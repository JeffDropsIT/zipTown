package com.devdesign.developer.ziptown.models.mockerClasses;

import java.io.Serializable;

public class Publisher implements Serializable {
    private  String name, contact;
    private  int id;

    public Publisher(String name, String contact, int id){
        this.name = name;
        this.contact = contact;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
