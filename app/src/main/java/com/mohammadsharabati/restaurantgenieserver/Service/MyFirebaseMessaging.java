package com.mohammadsharabati.restaurantgenieserver.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mohammadsharabati.restaurantgenieserver.Common.Common;
import com.mohammadsharabati.restaurantgenieserver.Helper.NotificationHelper;
import com.mohammadsharabati.restaurantgenieserver.MainActivity;
import com.mohammadsharabati.restaurantgenieserver.OrderStatus;
import com.mohammadsharabati.restaurantgenieserver.R;

import java.util.Map;
import java.util.Random;

import androidx.core.app.NotificationCompat;


public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                sendNotificationAPI26(remoteMessage);
            else
                sendNotification(remoteMessage);
        }
    }

    private void sendNotificationAPI26(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        // Go to Order List
        PendingIntent pendingIntent;
        NotificationHelper notificationHelper;
        Notification.Builder builder;

        if (Common.currentUser != null) {
            Intent intent = new Intent(this, OrderStatus.class);
            intent.putExtra(Common.PHONE_TEXT, Common.currentUser.getPhone());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationHelper = new NotificationHelper(this);
            builder = notificationHelper.edtEatItChannelNotification(title, message, pendingIntent, defaultSoundUri);

            // Get random Id for notification to show all notification
            notificationHelper.getManager().notify(new Random().nextInt(), builder.build());
        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notificationHelper = new NotificationHelper(this);
            builder = notificationHelper.edtEatItChannelNotification(title, message, defaultSoundUri);

            // Get random Id for notification to show all notification
            notificationHelper.getManager().notify(new Random().nextInt(), builder.build());
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String message = data.get("message");

        if (Common.currentUser != null) {
            Intent intent = new Intent(this, OrderStatus.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0, builder.build());

        } else {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_local_shipping_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0, builder.build());
        }
    }
}
