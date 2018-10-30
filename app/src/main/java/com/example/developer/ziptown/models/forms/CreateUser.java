package com.example.developer.ziptown.models.forms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUser {
    String password, fullName, city, userType, contact;
    final String URL = "/account/create";
    public CreateUser(String password, String fullName, String userType, String city, String contact) {
        this.contact = contact;
        this.password = password;
        this.city = city;
        this.userType = userType;
        this.fullName = fullName;
    }

    public String getURL() {
        return URL;
    }

    public String getCity() {
        return city;
    }

    public String getContact() {
        return contact;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
