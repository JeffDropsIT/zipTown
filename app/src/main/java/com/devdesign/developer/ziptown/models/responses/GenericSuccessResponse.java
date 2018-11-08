package com.devdesign.developer.ziptown.models.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericSuccessResponse {
    private String message;
    private int response;
    @JsonCreator
    public GenericSuccessResponse(@JsonProperty("message")String message, @JsonProperty("response")int response){
        this.message = message;
        this.response = response;
    }
    public String getError() {
        return message;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public void setError(String error) {
        this.message = error;
    }

    @Override
    public String toString() {
        return "message: "+message+" response: "+response;
    }
}
