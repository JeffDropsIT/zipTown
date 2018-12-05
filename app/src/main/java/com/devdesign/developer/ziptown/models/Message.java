package com.devdesign.developer.ziptown.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.devdesign.developer.ziptown.activities.MainActivity;

import java.net.URL;

public class Message {
    private int to, from;
    private String username;
    private String token;
    private String pMessageId, timeSent, message, status, url;
    private boolean isSender;



    public boolean isSender() {
        return  isSender;//from == Integer.valueOf(MainActivity.getString("userId"));
    }

    public void setIsSender(boolean isSender) {
        this.isSender = isSender;
    }

    public Message(String senderId, String to, String created, String payload, String status, String url, String token, boolean isSender){
        this.url = url;
        this.username = MainActivity.getString("username");
        this.timeSent = created;
        this.message = payload;
        this.status = status;
        this.token = token;
        this.isSender = isSender;
        this.to = Integer.valueOf(to) ;
        this.from = Integer.valueOf(senderId);
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

    public void setFrom(int from) {
        this.from = from;
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

    public int getFrom() {
        return from;
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
