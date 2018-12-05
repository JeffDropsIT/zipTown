package com.devdesign.developer.ziptown.models;

public class Chat {
    String username, lastSeen, lastMessage, token, url;
    int userId;
    public Chat(String username, int userId, String lastMessage, String lastSeen, String token, String url){
        this.url = url;
        this.lastMessage = lastMessage;
        this.lastSeen = lastSeen;
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getToken() {
        return token;
    }
}
