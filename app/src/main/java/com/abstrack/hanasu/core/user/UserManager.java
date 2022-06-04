package com.abstrack.hanasu.core.user;

import android.util.Log;
import androidx.annotation.NonNull;

import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.abstrack.hanasu.core.Flame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager{

    private static PublicUser currentPublicUser;
    private static PrivateUser currentPrivateUser;

    public static void setInitialValues(){
        updateUserData("public", "connectionStatus", ConnectionStatus.ONLINE);
        getFCMTokenAndUpdate();
    }

    public static void getFCMTokenAndUpdate(){
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(!task.isSuccessful()){
                    Log.d("Hanasu-UserManager", "Error getting data", task.getException());
                }

                updateUserData("public", "fcmToken", task.getResult());
            }
        });
    }

    public static void writeNewUser(String identifier) {
        PrivateUser newPrivateUser = new PrivateUser();
        PublicUser newPublicUser = new PublicUser(identifier);

        currentPrivateUser = newPrivateUser;
        currentPublicUser = newPublicUser;

        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getUid()).setValue(currentPublicUser);
        Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getUid()).setValue(currentPrivateUser);
    }

    public static void updateUserData(String side, String path, Object value) {
        Flame.getDataBaseReferenceWithPath(side).child("users").child(Flame.getFireAuth().getUid()).child(path).setValue(value);
    }

    public static PublicUser getCurrentPublicUser(){
        return currentPublicUser;
    }

    public static PrivateUser getCurrentPrivateUser(){
        return currentPrivateUser;
    }

    public static void setCurrentPublicUser(PublicUser publicUser) {
        currentPublicUser = publicUser;
    }

    public static void setCurrentPrivateUser(PrivateUser privateUser){
        currentPrivateUser = privateUser;
    }
}
