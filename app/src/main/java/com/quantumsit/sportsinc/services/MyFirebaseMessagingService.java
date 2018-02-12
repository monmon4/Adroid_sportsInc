package com.quantumsit.sportsinc.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quantumsit.sportsinc.Aaa_data.Config;
import com.quantumsit.sportsinc.Activities.HomeActivity;
import com.quantumsit.sportsinc.Activities.NotificationDetailsActivity;
import com.quantumsit.sportsinc.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 2/1/2018.
 */

public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "NotifiMessageService";

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.v(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.v(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            resultIntent.putExtra("Notification", Config.NOTIFICATION_ID);
            resultIntent.putExtra("message", message);

            showNotificationMessage(getApplicationContext(),Config.NOTIFICATION_ID, "", message, "", resultIntent);
        }else{
            // If the app is in background, firebase itself handles the notification

        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.v(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String timestamp = data.getString("timestamp");
            int notify_id = data.getInt("notify_id");

            Log.d(TAG, "title: " + title);
            Log.d(TAG, "message: " + message);
            Log.d(TAG, "Notify_id: " + notify_id);
            Log.d(TAG, "timestamp: " + timestamp);

            Intent resultIntent = new Intent(getApplicationContext(), NotificationDetailsActivity.class);
            // resultIntent.putExtra("Notification",Config.NOTIFICATION_ID);
            resultIntent.putExtra("notify_id", notify_id);

            showNotificationMessage(getApplicationContext(),notify_id , title, message, timestamp, resultIntent);

        } catch (JSONException e) {
            Log.v(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.v(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, int id , String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(id , title, message, timeStamp, intent);
    }

}