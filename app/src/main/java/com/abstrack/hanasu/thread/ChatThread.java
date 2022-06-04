package com.abstrack.hanasu.thread;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.message.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatThread extends  Thread{

    private Context ctx;
    private List<Chat> chatsList;

    public ChatThread(Context ctx){
        this.ctx = ctx;
        this.chatsList = new ArrayList<Chat>();
    }

    @Override
    public void run() {
        ValueEventListener chatInformationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    List<String> chatRoomsList = (List<String>) snapshot.getValue();

                    for(String chatUUID : chatRoomsList){
                        fetchChatRoomInformation(chatUUID);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hanasu-UserListener", "An error ocurred while retrieving Chat information", error.toException());
            }
        };

        Flame.getDataBaseReferenceWithPath("private").child("users").child(Flame.getFireAuth().getCurrentUser().getUid()).child("chatRoomList").addValueEventListener(chatInformationListener);
    }

    public void fetchChatRoomInformation(String chatUUID){
        Flame.getDataBaseReferenceWithPath("private").child("chatRooms").child(chatUUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d("Hanasu-ChatThread", "An error ocurred while retrieving ChatRoom information", task.getException());
                }

                if(task.getResult().getValue() != null){
                    ChatRoom chatRoom = task.getResult().getValue(ChatRoom.class);
                    Message message = chatRoom.getMessagesList().get(chatRoom.getMessagesList().size() - 1);
                    //addToChatList(new Chat("test", message.getMessageStatus(), message.getTime(), chatUUID, ));
                }
            }
        });
    }

    public void addToChatList(Chat chat) {
        chatsList.add(chat);
    }
}
