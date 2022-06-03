package com.abstrack.hanasu.core;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Flame {

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
