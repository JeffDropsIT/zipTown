package com.example.developer.ziptown.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse {

    private User user;
    private ArrayList<Offers> offers;
    private ArrayList<Requests> requests;
    @JsonCreator
    public CreateUserResponse(@JsonProperty("user") User user,@JsonProperty("offers") ArrayList<Offers> offers,@JsonProperty("requests")  ArrayList<Requests> requests){

        this.user = user;
        this.offers = offers;
        this.requests = requests;
    }

    public ArrayList<Offers> getOffers() {
        return offers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRequests(ArrayList<Requests> requests) {
        this.requests = requests;
    }

    public void setOffers(ArrayList<Offers> offers) {
        this.offers = offers;
    }

    public ArrayList<Requests> getRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "user: "+user+" requests: "+requests+" offers: "+offers;
    }
}
