package com.abstrack.hanasu.activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chatroom.message.MessageAdapter;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.chatroom.chat.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.chat.data.MessageType;
import com.abstrack.hanasu.core.chatroom.message.Message;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.abstrack.hanasu.db.FireDatabase;
import com.abstrack.hanasu.util.AndroidUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.ConnectException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends BaseAppActivity {

    private String chatRoom;
    private RecyclerView recyclerViewMessage;
    private List<Message> messageList;

    private TextView txtContactName, txtContactStatus;
    private ImageView imgContactProfilePicture;
    private EditText edtTxtMsg;

    private Animation down_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            chatRoom = extras.getString("chatRoom");
        }

        messageList = new ArrayList<>();

        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        txtContactName = findViewById(R.id.txtContactName);
        txtContactStatus = findViewById(R.id.txtContactStatus);
        imgContactProfilePicture = findViewById(R.id.imgContactProfilePicture);
        edtTxtMsg = findViewById(R.id.edtTxtMsg);

        loadChatInformation();
        loadFriendInformation();
    }

    public void sendMessage(View view) {
        if (!edtTxtMsg.getText().toString().isEmpty()) {
            DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom+"/messagesList");
            chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("HanasuChat", "Error getting values");
                    }

                    List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) task.getResult().getValue();
                    chatRoomRef.child(String.valueOf(messagesList.size())).setValue(new Message(edtTxtMsg.getText().toString(), AndroidUtil.getCurrentHour(), UserManager.getCurrentUser().getIdentifier(), MessageStatus.SENDING, MessageType.TEXT));
                    loadChatInformation();
                }
            });
        }
    }

    public void loadChatInformation() {
        edtTxtMsg.setText("");
        messageList.clear();

        DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
        chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("HanasuChat", "Error getting values");
                }

                List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) task.getResult().child("messagesList").getValue();
                for (int i = 1; i < messagesList.size(); i++) {

                    MessageType messageType = MessageType.valueOf(messagesList.get(i).get("messageType"));
                    MessageStatus messageStatus = MessageStatus.valueOf(messagesList.get(i).get("messageStatus"));
                    String content = messagesList.get(i).get("content");
                    String sentBy = messagesList.get(i).get("sentBy");
                    String time = messagesList.get(i).get("time");

                    Message model = new Message(content, time, sentBy, messageStatus, messageType);
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

    public void loadFriendInformation() {
        DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
        chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("HanasuChat", "Error getting values");
                }
                List<String> userList = (List<String>) task.getResult().child("users").getValue();

                for(String identifier : userList){
                    if(!identifier.equals(UserManager.getCurrentUser().getIdentifier())){
                        fetchFriendInformation(identifier);
                        break;
                    }
                }
            }
        });
    }

    public void fetchFriendInformation(String friendIdentifier) {
        FireDatabase.getDataBaseReferenceWithPath("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    return;
                }

                for (DataSnapshot user : task.getResult().getChildren()) {
                    if (!user.child("identifier").getValue().equals(friendIdentifier)) {
                        continue;
                    }

                    String contactName = (String) user.child("displayName").getValue();
                    String imgExtension = (String) user.child("imgExtension").getValue();
                    String imgKey = (String) user.child("imgKey").getValue();
                    ConnectionStatus connectionStatus = ConnectionStatus.valueOf(user.child("connectionStatus").getValue().toString());

                    txtContactName.setText(contactName);

                    decorateConnectionStatus(connectionStatus);
                    fetchFriendProfilePicture(imgExtension, imgKey);
                }
            }
        });
    }

    public void decorateConnectionStatus(ConnectionStatus connectionStatus){
        if(connectionStatus == ConnectionStatus.ONLINE){
            down_anim = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.down_movement);
            txtContactStatus.setVisibility(View.VISIBLE);
            txtContactStatus.setText("En línea");
            txtContactStatus.setAnimation(down_anim);
        } else {
            txtContactStatus.setVisibility(View.INVISIBLE);
        }
    }

    public void fetchFriendProfilePicture(String imgExtension, String imgKey) {
        String imagePath = "image/" + imgKey + imgExtension;

        StorageReference imgRef = FirebaseStorage.getInstance().getReference();
        imgRef.child(imagePath).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (!task.isSuccessful()) {
                    return;
                }

                if (imgExtension.equals(".gif")) {
                    Glide.with(ChatActivity.this).asGif().load(task.getResult()).into(imgContactProfilePicture);
                } else {
                    Glide.with(ChatActivity.this).asBitmap().load(task.getResult()).into(imgContactProfilePicture);
                }
            }
        });
    }

    public void returnToLastActivity(View view) {
        finish();
    }
}
