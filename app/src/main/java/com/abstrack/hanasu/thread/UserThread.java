package com.abstrack.hanasu.thread;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.PrivateUser;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UserThread extends Thread {

    private Context ctx;

    public UserThread(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public synchronized void start() {
        UserManager.setInitialValues();
        super.start();
    }

    @Override
    public void run() {
        ValueEventListener publicInformationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PublicUser publicUser = snapshot.getValue(PublicUser.class);
                UserManager.setCurrentPublicUser(publicUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserListener", "An error ocurred while retrieving Public User information", error.toException());
            }
        };

        ValueEventListener privateInformationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PrivateUser privateUser = snapshot.getValue(PrivateUser.class);
                UserManager.setCurrentPrivateUser(privateUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserListener", "An error ocurred while retrieving Private User information", error.toException());
            }
        };

        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(publicInformationListener);
        Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(privateInformationListener);
        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).child("connectionStatus").onDisconnect().setValue(ConnectionStatus.OFFLINE);
    }
}
