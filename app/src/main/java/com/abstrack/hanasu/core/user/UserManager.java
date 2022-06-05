package com.abstrack.hanasu.core.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.callback.OnContactDataReceiveCallback;
import com.abstrack.hanasu.callback.OnUserDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserManager {

    public static PublicUser currentPublicUser;
    public static PrivateUser currentPrivateUser;

    private static void startUp() {
        addConnectionListener();
        fetchFCMTokenAndUpdate();
    }

    public static void syncPublicAndPrivateData(OnUserDataReceiveCallback userDataReceiveCallback) {
        Log.d("Hanasu-UserManager", "Sync started");
        startUp();

        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PublicUser publicUser = snapshot.getValue(PublicUser.class);
                currentPublicUser = publicUser;

                userDataReceiveCallback.onDataReceiver(currentPublicUser);
                Log.d("Hanasu-UserManager", "(Public) synced");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserManager", "An error ocurred while retrieving Public User information", error.toException());
            }
        });

        Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PrivateUser privateUser = snapshot.getValue(PrivateUser.class);
                currentPrivateUser = privateUser;

                userDataReceiveCallback.onDataReceiver(currentPrivateUser);
                Log.d("Hanasu-UserManager", "(Private) synced");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserManager", "An error ocurred while retrieving Private User information", error.toException());
            }
        });
    }

    public static void fetchFCMTokenAndUpdate() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d("Hanasu-UserManager", "Error getting data", task.getException());
                }

                updateUserData("public", "fcmToken", task.getResult());
            }
        });
    }

    public static void addConnectionListener() {
        updateUserData("public", "connectionStatus", ConnectionStatus.ONLINE);
        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).child("connectionStatus").onDisconnect().setValue(ConnectionStatus.OFFLINE);
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

    public static void fetchContactPublicInformation(String contactIdentifier, OnContactDataReceiveCallback contactDataReceiveCallback){
        Log.d("Hanasu-UserManager", "Contact sync started");

        Flame.getDataBaseReferenceWithPath("public").child("users").orderByChild("identifier").equalTo(contactIdentifier).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot contactDataSnapshot : snapshot.getChildren()){
                    PublicUser contactPublicUser = contactDataSnapshot.getValue(PublicUser.class);
                    contactDataReceiveCallback.onDataReceive(contactPublicUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserManager", "An error ocurred while retrieving Public Contact information", error.toException());
            }
        });
    }
}
