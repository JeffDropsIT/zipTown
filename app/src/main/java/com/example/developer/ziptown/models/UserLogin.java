package com.example.developer.ziptown.models;

public class UserLogin {
    private String contact, password;
    final String URL = "/account/login";
    public UserLogin(String contact, String password){
        this.contact = contact;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getURL() {
        return URL;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
