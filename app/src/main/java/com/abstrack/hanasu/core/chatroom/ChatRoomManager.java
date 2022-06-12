package com.abstrack.hanasu.core.chatroom;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.callback.OnChatRoomDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.chat.ChatManager;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.user.PrivateUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatRoomManager {

    public static void syncPrivateData(OnChatRoomDataReceiveCallback chatRoomDataReceiveCallback){
        int listIndex = 0;
        ChatManager.getChatsList().clear();

        for(String chatRoomUUID : UserManager.currentPrivateUser.getChatRoomList().keySet()) {
            listIndex++;
            int finalListIndex = listIndex;

            if(!chatRoomUUID.equals("chatRoomUUID")){
                Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);

                        if(chatRoom != null){
                            chatRoomDataReceiveCallback.onDataReceiver(chatRoom);

                            if(finalListIndex == UserManager.currentPrivateUser.getChatRoomList().keySet().size()) {
                                chatRoomDataReceiveCallback.onDataReceived();
                                Log.d("Hanasu-ChatRoomManager", "(ChatRoom) Data Synced");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("Hanasu", "Me rehuso a poner logs");
                    }
                });
            }
        }
    }

    public static void fetchPrivateData(OnChatRoomDataReceiveCallback chatRoomDataReceiveCallback) {
        int listIndex = 0;
        ChatManager.getChatsList().clear();

        for(String chatRoomUUID : UserManager.currentPrivateUser.getChatRoomList().keySet()){
            listIndex++;
            int finalListIndex = listIndex;

            if(!chatRoomUUID.equals("chatRoomUUID")){
                Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.d("Hanasu-ChatRoomManager", "Error getting data");
                            return;
                        }

                        ChatRoom chatRoom = task.getResult().getValue(ChatRoom.class);

                        if(chatRoom != null) {
                            chatRoomDataReceiveCallback.onDataReceiver(chatRoom);

                            if(finalListIndex == UserManager.currentPrivateUser.getChatRoomList().keySet().size() - 1) {
                                chatRoomDataReceiveCallback.onDataReceived();
                                Log.d("Hanasu-ChatRoomManager", "(ChatRoom) Data fetched");
                            }
                        }
                    }
                });
            }
        }
    }

    public static void syncPrivateDataByIdentifier(String chatRoomUUID, OnChatRoomDataReceiveCallback chatRoomDataReceiveCallback) {
        Log.d("Hanasu-ChatRoomManager", "Specific ChatRoom sync started");

        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);

                if(chatRoom != null){
                    chatRoomDataReceiveCallback.onDataReceiver(chatRoom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-ChatRoomManager", "An error ocurred while retrieving ChatRoom information", error.toException());
            }
        });
    }


    public static void writeNewIndividualChatRoom(String chatRoomUUID, ChatType chatType, String contactIdentifier){
        ChatRoom chatRoom = new ChatRoom(chatRoomUUID, chatType, contactIdentifier);
        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID.toString()).setValue(chatRoom);
    }

    public static void writeNewGroupalChatRoom(String chatRoomUUID, ChatType chatType, String chatImgKey, String contactIdentifier){
        ChatRoom chatRoom = new ChatRoom(chatRoomUUID, chatType, chatImgKey, contactIdentifier);
        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatRoomUUID.toString()).setValue(chatRoom);
    }
}
