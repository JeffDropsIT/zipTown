package com.example.developer.ziptown.models.responses;

import com.example.developer.ziptown.models.objectModels.Offers;
import com.example.developer.ziptown.models.objectModels.Requests;
import com.example.developer.ziptown.models.objectModels.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSignInAndLoginResponse implements Serializable {
    private User user;
    private ArrayList<Offers> offers;
    private ArrayList<Requests> requests;
    public static ObjectMapper oMapper = new ObjectMapper();
    @JsonCreator
    public UserSignInAndLoginResponse(@JsonProperty("user")User user, @JsonProperty("offers") ArrayList<Offers> offers, @JsonProperty("requests") ArrayList<Requests> requests) {

        this.user = user;
        this.offers = offers;
        this.requests = requests;
    }

    public Map<String, Object> ObjectToMap(Object object){
        Map<String, Object> map = oMapper.convertValue(object, Map.class);
        return map;
    }

    public Map<String, Object> getUserMap(){
        return ObjectToMap(user);
    }
    public Map<String, Object> getOffersMap(){
        Map<String, Object> tempMap = new HashMap<>();
        for (int i = 0; i < offers.size(); i ++){
            tempMap.put(String.valueOf(offers.get(i).getId()), ObjectToMap(offers.get(i)));
        }
        return tempMap;
    }
    public Map<String, Object> getRequestsMap(){
        Map<String, Object> tempMap = new HashMap<>();
        for (int i = 0; i < requests.size(); i ++){
            tempMap.put(String.valueOf(requests.get(i).getId()), ObjectToMap(requests.get(i)));
        }
        return tempMap;
    }
    public ArrayList<Offers> getOffers() {
        return offers;
    }

    public ArrayList<Integer> getOffersIdSet(){
        ArrayList<Integer> offerIdSet =  new ArrayList<>();
        for(int i = 0; i < offers.size(); i++){
            offerIdSet.add(offers.get(i).getId());
        }
        return offerIdSet;
    }
    public ArrayList<Integer> getRequestIdSet(){
        ArrayList<Integer> offerIdSet =  new ArrayList<>();
        for(int i = 0; i < requests.size(); i++){
            offerIdSet.add(requests.get(i).getId());
        }
        return offerIdSet;
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
