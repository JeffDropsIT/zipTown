package com.example.developer.ziptown.models.baseClasses;

import java.io.Serializable;

public abstract class BasePost implements Serializable {
    private String _id;
    private String city;
    private String contact;
    private String created;
    private String postType;
    private String publisher;
    private int publisherId;
    private String depatureTime;
    private String returnTime;
    private String origin;
    private String destination;
    private String days;
    private int id;

    public BasePost() {

    }
    public BasePost(String city, String contact, String postType, int publisherId, String publisher,
                    String returnTime, String destination, String days, String origin, String depatureTime ){
        this.city = city;
        this.contact = contact;
        this.postType = postType;
        this.days = days;
        this.publisher = publisher;
        this.publisherId = publisherId;
        this.returnTime = returnTime;
        this.depatureTime = depatureTime;
        this.origin = origin;
        this.destination = destination;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPostType() {
        return postType;
    }

    public String getDepatureTime() {
        return depatureTime;
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

    public int getId() {
        return id;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public void setDepatureTime(String depatureTime) {
        this.depatureTime = depatureTime;
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

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public String getCity() {
        return city;
    }
}
