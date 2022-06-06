package com.abstrack.hanasu.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.abstrack.hanasu.R;

public class MessageNotifier {

    public static void createNotificationChannel(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Hanasu-Message-Channel";
            String description = "Canal de notificación para mensajes de Hanasu";
            NotificationChannel channel = new NotificationChannel("Hanasu-Message-Channel", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);

            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static Notification builder(Context ctx, String messageTitle, String contentText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, "Hanasu-Message-Channel")
                .setSmallIcon(R.drawable.ic_add_chat)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder.build();
    }
}
