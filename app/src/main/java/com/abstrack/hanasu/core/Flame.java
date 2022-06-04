package com.abstrack.hanasu.core;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.core.user.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Flame {

    public static String getFCMToken() {
        return FirebaseMessaging.getInstance().getToken().getResult();
    }

    public static FirebaseDatabase getFireDatabase(){
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getDataBaseReferenceWithPath(String path){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(path);
        dbReference.keepSynced(true);
        return dbReference;
    }

    public static DatabaseReference getDataBaseReferenceWithPath(String path, boolean synced){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(path);
        dbReference.keepSynced(synced);
        return dbReference;
    }

    public static StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseAuth getFireAuth() {
        return FirebaseAuth.getInstance();
    }

    public static boolean isFireUserLogged(){
        if(getFireAuth().getCurrentUser() != null){
            return true;
        }

        return false;
    }
}
