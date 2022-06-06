package com.abstrack.hanasu.service;

import android.util.Log;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.UserManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);

        if(Flame.isFireUserLogged()) {
            UserManager.updateUserData("public", "fcmToken", newToken);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Test", "watafak");
        super.onMessageReceived(remoteMessage);
    }
}
