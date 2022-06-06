package com.abstrack.hanasu.service;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.notification.MessageNotifier;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MessagingService extends FirebaseMessagingService {

    private Random random = new Random();

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);

        if(Flame.isFireUserLogged()) {
            UserManager.updateUserData("public", "fcmToken", newToken);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() != null){
            if(remoteMessage.getData().get("chatRoomUUID") != null){
                UserManager.addNewContact(remoteMessage.getData().get("chatRoomUUID"), remoteMessage.getData().get("contactIdentifier"));
            }

            MessageNotifier.createNotificationChannel(this);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(random.nextInt(), MessageNotifier.builder(this, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()));
        }
    }
}
