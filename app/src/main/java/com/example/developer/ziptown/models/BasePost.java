package com.example.developer.ziptown.models;

abstract class BasePost {
    private String _id, city, contact, created, postType, publisher, publisherId, depatureTime, returnTime, origin, destination, days;
    private int id;

    public BasePost() {

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

    public String getPublisherId() {
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

    public void setPublisherId(String publisherId) {
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
