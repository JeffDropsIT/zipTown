package com.example.developer.ziptown.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLoginResponse {
    private String message;
    private int response;
    private Data data;
    @JsonCreator
    public UserLoginResponse(@JsonProperty("message")String message, @JsonProperty("response")int response, @JsonProperty("data") Data data){
        this.data = data;
        this.message = message;
        this.response = response;
    }



    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponse(int response) {
        this.response = response;
    }

}
