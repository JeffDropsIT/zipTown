package com.devdesign.developer.ziptown.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.devdesign.developer.ziptown.activities.profileActivities.MessengerActivity.TAG;

public class FCMBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: received broadcast: "+intent.getAction());


    }
}
