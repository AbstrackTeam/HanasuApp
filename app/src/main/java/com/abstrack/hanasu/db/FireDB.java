package com.abstrack.hanasu.db;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

public class FireDB {

    public static FirebaseDatabase getFbDatabase(){
        return FirebaseDatabase.getInstance();
    }

}
