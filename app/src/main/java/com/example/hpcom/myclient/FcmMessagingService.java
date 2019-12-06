package com.example.hpcom.myclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by hp.com on 3/25/2018.
 */

public class FcmMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.i("TAAG","Notification received1\n");
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            Log.i("TAAG","Notification received2\n");
            Intent intent = new Intent(this, MainActivity.class);
            Log.i("TAAG","Notification received3\n");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
           /* NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(message);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());*/
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "M_CH_ID");
            Log.i("TAAG","Notification received4\n");
            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setTicker("Hearty365")
                    .setPriority(Notification.PRIORITY_MAX) // this is deprecated in API 26 but you can still use for below 26. check below update for 26 API
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentInfo("Info");
            Log.i("TAAG","Notification received5\n");
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.i("TAAG","Notification received6\n");
            notificationManager.notify(1, notificationBuilder.build());
            Log.i("TAAG","Notification received7\n");
            super.onMessageReceived(remoteMessage);
        }
        catch(Exception e)
        {}
    }
}
