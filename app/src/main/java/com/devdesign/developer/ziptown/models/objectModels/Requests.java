package com.devdesign.developer.ziptown.models.objectModels;

import com.devdesign.developer.ziptown.models.baseClasses.BasePost;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Requests  extends BasePost {


    public Requests() {
        super();
    }

    @Override
    public int getId() {
        return super.getId();
    }


    @Override
    public String getCity() {
        return super.getCity();
    }

    @Override
    public String getContact() {
        return super.getContact();
    }

    @Override
    public String getCreated() {
        return super.getCreated();
    }

    @Override
    public String getDays() {
        return super.getDays();
    }

    @Override
    public String getDepatureTime() {
        return super.getDepatureTime();
    }

    @Override
    public String getDestination() {
        return super.getDestination();
    }

    @Override
    public String getReturnTime() {
        return super.getReturnTime();
    }

    @Override
    public int getPublisherId() {
        return super.getPublisherId();
    }

    @Override
    public String getPublisher() {
        return super.getPublisher();
    }

    @Override
    public String getPostType() {
        return super.getPostType();
    }

    @Override
    public String getOrigin() {
        return super.getOrigin();
    }

    @Override
    public void setCity(String city) {
        super.setCity(city);
    }

    @Override
    public void setContact(String contact) {
        super.setContact(contact);
    }

    @Override
    public void setCreated(String created) {
        super.setCreated(created);
    }

    @Override
    public void setDays(String days) {
        super.setDays(days);
    }

    @Override
    public void setDepatureTime(String depatureTime) {
        super.setDepatureTime(depatureTime);
    }

    @Override
    public void setDestination(String destination) {
        super.setDestination(destination);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public void setOrigin(String origin) {
        super.setOrigin(origin);
    }

    @Override
    public void setPostType(String postType) {
        super.setPostType(postType);
    }

    @Override
    public void setPublisher(String publisher) {
        super.setPublisher(publisher);
    }

    @Override
    public void setPublisherId(int publisherId) {
        super.setPublisherId(publisherId);
    }

    @Override
    public void setReturnTime(String returnTime) {
        super.setReturnTime(returnTime);
    }

    @Override
    public Map<String, Object> ObjectToMap(Object object) {
        return super.ObjectToMap(object);
    }
}
