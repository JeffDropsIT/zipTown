package com.example.developer.ziptown.models.objectModels;



public class User {
    private String _id, fullName, city, contact, created, userType;
    private int id;

    public User(){

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

    public String get_id() {
        return _id;
    }

    public int getId() {
        return id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
}
