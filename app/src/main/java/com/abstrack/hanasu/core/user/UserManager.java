package com.abstrack.hanasu.core.user;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.callback.OnContactDataReceiveCallback;
import com.abstrack.hanasu.callback.OnUserDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageType;
import com.abstrack.hanasu.core.contact.ContactManager;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class UserManager {

    public static PublicUser currentPublicUser;
    public static PrivateUser currentPrivateUser;

    public static void initInitialValues() {
        addConnectionListener();
        fetchFCMTokenAndUpdate();
    }

    public static void addNewContact(String chatRoomUUID, String contactIdentifier) {
        updateUserData("private", "contacts", retrieveNewContactList(contactIdentifier, chatRoomUUID));
        updateUserData("private", "chatRoomList", retrieveNewChatRoomList(chatRoomUUID));
    }

    public static void sendFriendRequest(String chatRoomUUID, String contactIdentifier) {
        UserManager.fetchContactPublicInformation(contactIdentifier, new OnContactDataReceiveCallback() {
            @Override
            public void onDataReceive(PublicUser contactPublicUser) {
                Flame.sendFriendRequestNotification(UserManager.currentPublicUser.getDisplayName(), "Te ha añadido como contacto", chatRoomUUID, contactPublicUser.getFcmToken());
            }
        });
    }

    public static void sendMessage(ChatRoom chatRoom, PublicUser publicContactUser, EditText edtTxtMsg) {
        if (chatRoom == null || publicContactUser == null) {
            return;
        }

        if (!edtTxtMsg.getText().toString().isEmpty()) {
            Message message = new Message(edtTxtMsg.getText().toString(), AndroidUtil.getCurrentHour(), UserManager.currentPublicUser.getIdentifier(), MessageStatus.ARRIVED_NOT_SEEN, MessageType.TEXT);

            edtTxtMsg.setText("");
            chatRoom.getMessagesList().add(message);

            Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoom.getChatRoomUUID()).child("messagesList").setValue(chatRoom.getMessagesList());
            Flame.sendMessageNotification(message.getSentBy(), message.getContent(), publicContactUser.getFcmToken());
        }
    }

    public static void fetchPublicAndPrivateData(OnUserDataReceiveCallback userDataReceiveCallback) {
        if (Flame.getFireAuth().getCurrentUser() != null) {
            Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {

                if (!task.isSuccessful()) {
                    Log.d("Hanasu-UserManager", "(PublicUser) Error getting data");
                    return;
                }

                PublicUser publicUser = task.getResult().getValue(PublicUser.class);

                if (publicUser != null) {
                    currentPublicUser = publicUser;
                    Log.d("Hanasu-UserManager", "(PublicUser) Data fetched");
                    userDataReceiveCallback.onDataReceiver(currentPublicUser);

                    Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).get().addOnCompleteListener(task1 -> {

                        if (!task1.isSuccessful()) {
                            Log.d("Hanasu-UserManager", "(PrivateUser) Error getting data");
                            return;
                        }

                        PrivateUser privateUser = task1.getResult().getValue(PrivateUser.class);

                        if (privateUser != null) {
                            currentPrivateUser = privateUser;
                            Log.d("Hanasu-UserManager", "(PrivateUser) Data fetched");
                            userDataReceiveCallback.onDataReceiver(privateUser);
                        }
                    });
                }
            });
        }
    }

    public static void syncPublicAndPrivateData(OnUserDataReceiveCallback userDataReceiveCallback) {
        if(Flame.getFireAuth().getCurrentUser() != null) {
            Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PublicUser publicUser = snapshot.getValue(PublicUser.class);

                    if(publicUser != null){
                        currentPublicUser = publicUser;
                        userDataReceiveCallback.onDataReceiver(currentPublicUser);
                        Log.d("Hanasu-UserManager", "(PublicUser) Data synced");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Hanasu-UserManager", "Error getting data");
                }
            });

            Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot userData : snapshot.getChildren()){
                        PrivateUser privateUser = userData.getValue(PrivateUser.class);

                        if(privateUser != null){
                            currentPrivateUser = privateUser;
                            userDataReceiveCallback.onDataReceiver(currentPrivateUser);
                            Log.d("Hanasu-UserManager", "(PrivateUser) Data synced");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Hanasu-UserManager", "Error getting data");
                }
            });
        }
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

    public static void fetchAndListenContactPublicInformation(String
                                                                      contactIdentifier, OnContactDataReceiveCallback contactDataReceiveCallback) {
        Log.d("Hanasu-UserManager", "Contact sync started");

        Flame.getDataBaseReferenceWithPath("public").child("users").orderByChild("identifier").equalTo(contactIdentifier).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot contactDataSnapshot : snapshot.getChildren()) {
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

    public static void fetchContactPublicInformation(String contactIdentifier, OnContactDataReceiveCallback contactDataReceiveCallback) {
        Log.d("Hanasu-UserManager", "Contact get data started");

        Flame.getDataBaseReferenceWithPath("public").child("users").orderByChild("identifier").equalTo(contactIdentifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("Hanasu-UserManager", "Error getting data", task.getException());
                }

                PublicUser contactPublicUser = task.getResult().getValue(PublicUser.class);
                contactDataReceiveCallback.onDataReceive(contactPublicUser);
            }
        });
    }

    public static HashMap<String, String> retrieveNewContactList(String friendIdentifier, String chatRoomUUID) {
        HashMap<String, String> newContactsList = UserManager.currentPrivateUser.getContacts();
        newContactsList.put(friendIdentifier, chatRoomUUID);
        return newContactsList;
    }

    public static HashMap<String, Integer> retrieveNewChatRoomList(String chatRoomUUID) {
        HashMap<String, Integer> newChatRoomList = UserManager.currentPrivateUser.getChatRoomList();
        newChatRoomList.put(chatRoomUUID, newChatRoomList.size());
        return newChatRoomList;
    }
}
