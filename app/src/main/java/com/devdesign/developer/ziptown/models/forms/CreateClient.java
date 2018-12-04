package com.devdesign.developer.ziptown.models.forms;

public class CreateClient {
    String token;
    int userId;
    private final String URL = "/account/user/data/client";

    public CreateClient(){

    }
    public CreateClient(String token, int userId){
        this.token = token;
        this.userId = userId;
    }
    public CreateClient(String token){
        this.token = token;
    }
    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getURL() {
        return URL;
    }
}
