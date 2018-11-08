package com.devdesign.developer.ziptown.models.objectModels;


import com.devdesign.developer.ziptown.models.responses.UserSignInAndLoginResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private String  fullName, city, contact, created, userType, password;
    private int id;

    public User(){

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserType() {
        return userType;
    }

    public String getCreated() {
        return created;
    }


    public int getId() {
        return id;
    }


    public void setCreated(String created) {
        this.created = created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public Map<String, Object> ObjectToMap(Object object){
        Map<String, Object> map = UserSignInAndLoginResponse.oMapper.convertValue(object, Map.class);
        return map;
    }
}
