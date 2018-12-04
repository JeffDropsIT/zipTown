package com.devdesign.developer.ziptown.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.devdesign.developer.ziptown.R;
import com.devdesign.developer.ziptown.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.devdesign.developer.ziptown.activities.profileActivities.MessengerActivity.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static final String CHANNEL_ID = "001";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived: received message: "+remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            switch (data.get("event")){
                case "message":
                    Log.i(TAG, "onMessageReceived: message received");
                    try {
                        JSONObject object = new JSONObject(data.get("payload"));
                        createNotification("message", object.get("username")+": "+object.get("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            default:
                Log.i(TAG, "onMessageReceived: default "+data);
                break;
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        Log.i(TAG, "onNewToken: "+s);
        MainActivity.putString("token", s);
        Intent intent = new Intent();
        intent.setAction("ziptown.action.newToken");
        intent.putExtra("token",s);
        sendBroadcast(intent);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void createNotification(String title, String msg){
        createNotificationChannel();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(055, mBuilder.build());
    }
}
