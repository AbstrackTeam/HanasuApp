package com.abstrack.hanasu.thread;

import android.util.Log;
import androidx.annotation.NonNull;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.user.User;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.abstrack.hanasu.core.Flame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class UserServiceThread extends Thread{
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
                ConnectionStatus connectionStatus = ConnectionStatus.valueOf(data.child("connectionStatus").getValue().toString());
                // User added a friend
                if(contacts.size() > UserManager.getCurrentUser().getContacts().size()){

                }
                // User deleted a friend
                if(contacts.size() < UserManager.getCurrentUser().getContacts().size()){

                }
                // User updated some info
                UserManager.setCurrentUser(new User(name, tag, imgKey, imgExtension, about, identifier, uid, displayName, contacts, connectionStatus));
                Log.w("Hanasu-UserService", "currentUser updated.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ValueEventListener contactsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                UserManager.updateUserData("connectionStatus", ConnectionStatus.ONLINE);
                HashMap<String, String> contacts = (HashMap<String, String>) data.getValue();

                User currentUser = UserManager.getCurrentUser();

                String name = currentUser.getName();
                String tag = currentUser.getTag();
                String imgKey = currentUser.getImgKey();
                String imgExtension = currentUser.getImgExtension();
                String about = currentUser.getAbout();
                String identifier = name + tag;
                String uid = AuthManager.getFireAuth().getUid();
                String displayName = currentUser.getDisplayName();
                ConnectionStatus connectionStatus = currentUser.getConnectionStatus();

                UserManager.setCurrentUser(new User(name, tag, imgKey, imgExtension, about, identifier, uid, displayName, contacts, connectionStatus));
                UserManager.addConnectionListener();
                Log.w("Hanasu-UserService", "currentUser contacts updated.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "loadPost:onCancelled", error.toException());

            }
        };

        String identifier = UserManager.getCurrentUser().getIdentifier();
        Flame.getDataBaseReferenceWithPath("users").child(identifier).addValueEventListener(postListener);
        Flame.getDataBaseReferenceWithPath("users").child(identifier).child("contacts").addValueEventListener(contactsListener);
    }
}
