package com.devdesign.developer.ziptown.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

public class Message {
    private String pMessageId, userId, timeSent, message, status, url;
    private boolean isSender;



    public boolean isSender() {
        return isSender;
    }

    public void setIsSender(boolean isSender) {
        this.isSender = isSender;
    }

    public Message(String senderId, String created, String payload, String status, String url, boolean isSender){
        this.url = url;
        this.isSender = isSender;
        this.timeSent = created;
        this.message = payload;
        this.status = status;
        this.userId = senderId;
    }


    public String getTimeSent() {
        return timeSent;
    }

    public String getpMessageId() {
        return pMessageId;
    }
    public String getMessage() {
        return message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setpMessageId(String pMessageId) {
        this.pMessageId = pMessageId;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getUserId() {
        return userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Bitmap getProfileBitmap() {
        if(url == null)
            return null;
        URL newurl = null;
        try {
            newurl = new URL(url);
            return BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
