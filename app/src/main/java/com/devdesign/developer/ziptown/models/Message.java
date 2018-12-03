package com.devdesign.developer.ziptown.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

public class Message {
    private String messageId, senderId, created, payload, status, url;
    private boolean isSender;



    public boolean isSender() {
        return isSender;
    }

    public void setIsSender(boolean isSender) {
        this.isSender = isSender;
    }

    public Message(String messageId, String senderId, String created, String payload, String status, String url, boolean isSender){
        this.url = url;
        this.isSender = isSender;
        this.created = created;
        this.payload = payload;
        this.status = status;
        this.messageId = messageId;
        this.senderId = senderId;
    }

    public String getCreated() {
        return created;
    }

    public String getMessageId() {
        return messageId;
    }
    public String getPayload() {
        return payload;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSenderId() {
        return senderId;
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
