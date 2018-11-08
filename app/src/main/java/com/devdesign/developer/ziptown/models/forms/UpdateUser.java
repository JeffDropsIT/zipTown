package com.devdesign.developer.ziptown.models.forms;

import android.util.Log;

import com.devdesign.developer.ziptown.activities.MainActivity;

public class UpdateUser {
    final String URL = "/account/user/edt/"+ MainActivity.getString("userId");
    private String fullName, city, userType, id;
    public UpdateUser(String fullName, String city, String userType){
        this.city = city;
        this.fullName = fullName;
        this.userType = userType;
        this.id = MainActivity.getString("userId");
    }

    public String getURL() {
        return URL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean hasChanged(){
        String oldFullName, oldCity, oldUserType;
        oldUserType = MainActivity.getString("userType");
        oldCity = MainActivity.getString("city");
        oldFullName = MainActivity.getString("username");
        Log.i("WSX", "hasChanged: city "+oldCity.contains(city) +" fullName: "+fullName+" oldn "+oldFullName+"  "+oldFullName.contains(fullName) +" userType "+ oldUserType.contains(userType));
        if(oldCity.contains(city) && oldFullName.contains(fullName) && oldUserType.contains(userType)){
            return false;
        }else {
            return true;
        }
    }
    public String getCity() {
        return city;
    }

    public String getUserType() {
        return userType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
