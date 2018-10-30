package com.example.developer.ziptown.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericErrorResponse {
    private String error;
    private int response;
    @JsonCreator
    public GenericErrorResponse(@JsonProperty("error")String error, @JsonProperty("response")int response){
        this.error = error;
        this.response = response;
    }
    public String getError() {
        return error;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "error: "+error+" response: "+response;
    }
}
