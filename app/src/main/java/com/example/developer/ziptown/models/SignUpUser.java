package com.example.developer.ziptown.models;

public class SignUpUser {
    String password, name, city, selectedItem, contact;
    public SignUpUser(String password, String name, String selectedItem, String city, String contact) {
        this.contact = contact;
        this.password = password;
        this.city = city;
        this.selectedItem = selectedItem;
        this.name = name;
    }



}
