package com.abstrack.hanasu.activity.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chat.MessageAdapter;
import com.abstrack.hanasu.core.user.chat.MessageStatus;
import com.abstrack.hanasu.core.user.chat.MessageType;
import com.abstrack.hanasu.core.user.model.MessageModel;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends BaseAppActivity {

    private String chatRoom;
    private RecyclerView recyclerViewMessage;
    private List<MessageModel> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageList = new ArrayList<>();

        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatRoom = extras.getString("chatRoom");
        }

        loadChatInformation();
    }

    public void loadChatInformation() {
        messageList.clear();

        DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
        chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("HanasuChat", "Error getting values");
                }

                List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) task.getResult().child("messagesList").getValue();
                for(int i = 1; i < messagesList.size(); i++){

                    MessageType messageType = MessageType.valueOf(messagesList.get(i).get("messageType"));
                    MessageStatus messageStatus = MessageStatus.valueOf(messagesList.get(i).get("messageStatus"));
                    String content = messagesList.get(i).get("content");
                    String sentBy = messagesList.get(i).get("sentBy");
                    String time = messagesList.get(i).get("time");

                    MessageModel model = new MessageModel(content, time, sentBy, messageStatus, messageType);
                    messageList.add(model);
                }

                buildMessageRecyclerView();
            }
        });
    }

    public void buildMessageRecyclerView() {
        MessageAdapter messageAdapter = new MessageAdapter(messageList, ChatActivity.this);
        recyclerViewMessage.setAdapter((messageAdapter));
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    public void loadUserInformation() {

    }

    public void returnToLastActivity(View view){
        finish();
    }
}
