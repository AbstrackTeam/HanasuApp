package com.abstrack.hanasu.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireDatabase {

    public static DatabaseReference getDataBaseReferenceWithPath(String path){
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference(path);
        dbReference.keepSynced(true);
        return dbReference;
    }

    public static StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }
}
