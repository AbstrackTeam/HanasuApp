package com.abstrack.hanasu.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.abstrack.hanasu.R;

public class NotificationBuilder {

    public static void createNotificationChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HanasuNotification";
            String description = "Channel for all Hanasu Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("HanasuNotification", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void notifyMessage(Context ctx, String textTitle, String textContent, int iconID){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "HanasuNotification")
                .setSmallIcon(iconID)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(ctx);
        notificationManagerCompat.notify(100, builder.build());
    }

}
