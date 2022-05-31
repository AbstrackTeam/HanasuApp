package com.abstrack.hanasu.core.user;

import android.util.Log;
import androidx.annotation.NonNull;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager{

    private static User currentUser;

    public static void fetchInitialUserData() {
        FireDatabase.getFbDatabase().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }
                for(DataSnapshot user : task.getResult().getChildren()){
                    if(!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())){
                        continue;
                    }

                    String name = (String) user.child("name").getValue();
                    String tag = (String) user.child("tag").getValue();
                    String imgKey = (String) user.child("imgKey").getValue();
                    String imgExtension = (String) user.child("imgExtension").getValue();
                    String about = (String) user.child("about").getValue();
                    String identifier = name + tag;
                    String uid = AuthManager.getFireAuth().getUid();
                    String displayName = (String) user.child("displayName").getValue();
                    HashMap<String, String> contacts = (HashMap<String, String>) user.child("contacts").getValue();

                    currentUser = new User(name, tag, imgKey, imgExtension, about, identifier, uid, displayName, contacts);
                }
            }
        });
    }

    public static void writeNewUser(String name, String tag) {
        String identifier = name + tag;
        User userModel = new User(name, tag);
        currentUser = new User(name, tag);

        FireDatabase.getDataBaseReferenceWithPath("users").child(identifier).setValue(userModel);
    }

    public static void updateUserData(String path, String value) {
        if(currentUser != null) {
            FireDatabase.getDataBaseReferenceWithPath("users").child(currentUser.getIdentifier()).child(path).setValue(value);
        }
    }

    public static void addToUserContacts(String contactIdentifier){
        if(currentUser != null) {
            HashMap<String, String> currentContacts = getCurrentUser().getContacts();
            DatabaseReference userRef = FireDatabase.getDataBaseReferenceWithPath("users").child(currentUser.getIdentifier());
            DatabaseReference chatRoomsRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms");

            // Check if you haven't been added by that person.
            chatRoomsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                boolean hasChatRoom = false;
                String chatRoomKey = UUID.randomUUID().toString();
                String ownIdentifier = UserManager.getCurrentUser().getIdentifier();

                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(!task.isSuccessful()){
                        Log.e("firebase", "Error getting data", task.getException());
                        return;
                    }

                    for (DataSnapshot chatRoom : task.getResult().getChildren()){
                        if(((List<?>) chatRoom.child("users").getValue()).contains(ownIdentifier)){
                            if(((List<?>) chatRoom.child("users").getValue()).contains(contactIdentifier)){
                                hasChatRoom = true;
                                chatRoomKey = chatRoom.child("chatRoomKey").getValue().toString();
                                break;
                            }
                        }
                    }

                    if(hasChatRoom){
                        currentContacts.put(contactIdentifier, chatRoomKey);
                        userRef.child("contacts").setValue(currentContacts);
                        return;
                    }

                    // Create chat room
                    List<String> users = new ArrayList<String>();

                    users.add(ownIdentifier);
                    users.add(contactIdentifier);

                    currentContacts.put(contactIdentifier, chatRoomKey);
                    chatRoomsRef.child(chatRoomKey).setValue(new ChatRoom(users, chatRoomKey));
                    userRef.child("contacts").setValue(currentContacts);
                }
            });
        }
    }

    public static User getCurrentUser(){
        return currentUser;
    }

    public static void setCurrentUser(User user) { currentUser = user; }
}
