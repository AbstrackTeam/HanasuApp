package com.abstrack.hanasu.core.user;

import android.util.Log;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserService extends Thread{
    @Override
    public void run() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                String name = (String) data.child("name").getValue();
                String tag = (String) data.child("tag").getValue();
                String imgKey = (String) data.child("imgKey").getValue();
                String imgExtension = (String) data.child("imgExtension").getValue();
                String about = (String) data.child("about").getValue();
                String identifier = name + tag;
                String uid = AuthManager.getFireAuth().getUid();
                String displayName = (String) data.child("displayName").getValue();
                HashMap<String, String> contacts = (HashMap<String, String>) data.child("contacts").getValue();

                UserManager.setCurrentUser(new User(name, tag, imgKey, imgExtension, about, identifier, uid, displayName, contacts));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };

        String identifier = UserManager.getCurrentUser().getIdentifier();
        FireDatabase.getDataBaseReferenceWithPath("users").child(identifier).addValueEventListener(postListener);
    }
}
