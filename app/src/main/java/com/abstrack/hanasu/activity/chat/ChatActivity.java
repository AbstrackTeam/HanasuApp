package com.abstrack.hanasu.activity.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chatroom.message.MessageAdapter;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.chatroom.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.message.data.MessageType;
import com.abstrack.hanasu.core.chatroom.message.Message;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.abstrack.hanasu.db.FireDatabase;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.ImageUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends BaseAppActivity {

    private String chatRoom;
    private RecyclerView recyclerViewMessage;

    private TextView txtContactName, txtContactStatus;
    private ImageView imgContactProfilePicture;
    private EditText edtTxtMsg;

    private Animation down_anim, up_anim;
    private boolean firstAnimationRun = true;

    private List<Message> messageList;

    private final int PICK_PHOTO_GALLERY_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            chatRoom = extras.getString("chatRoom");
        }

        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        txtContactName = findViewById(R.id.txtContactName);
        txtContactStatus = findViewById(R.id.txtContactStatus);
        imgContactProfilePicture = findViewById(R.id.imgContactProfilePicture);
        edtTxtMsg = findViewById(R.id.edtTxtMsg);

        messageList = new ArrayList<Message>();

        loadChatInformation();
        loadFriendInformation();
        syncMessages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_PHOTO_GALLERY_CODE){
                uploadPhoto(data.getData(), ImageUtil.getMimeType(this, data.getData()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void pickPhotoFromGallery(View view){
        AndroidUtil.chooseGalleryPhoto(this, "Select profile picture", PICK_PHOTO_GALLERY_CODE);
    }

    public void uploadPhoto(Uri imgUri, String imgExtension){
        String imgKey = UUID.randomUUID().toString();
        final boolean[] progressFirstRun = {false};

        StorageReference imageStorageReference = FireDatabase.getStorageReference().child("image").child(imgKey + imgExtension);
        imageStorageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("HanasuStorage", "Image uploaded successfully");
                if(taskSnapshot.getMetadata() != null){
                    if(taskSnapshot.getMetadata().getReference() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                sendMessagePhoto(imageUrl);
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("HanasuStorage", "Image failed to upload", e);
            }
        });
    }

    public void sendMessagePhoto(String photoUrl) {
        DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom+"/messagesList");
        chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("HanasuChat", "Error getting values");
                }

                List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) task.getResult().getValue();
                chatRoomRef.child(String.valueOf(messagesList.size())).setValue(new Message(photoUrl, AndroidUtil.getCurrentHour(), UserManager.getCurrentUser().getIdentifier(), MessageStatus.SENDING, MessageType.IMAGE));
                edtTxtMsg.setText("");
            }
        });
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
                    edtTxtMsg.setText("");
                }
            });
        }
    }

    public void syncMessages() {
        DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);
        chatRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {

                List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) data.child("messagesList").getValue();
                if ((messagesList.size() - 1) != messageList.size()) {
                    HashMap<String, String> lastMessage = messagesList.get(messagesList.size() - 1);

                    MessageAdapter adapter = (MessageAdapter) recyclerViewMessage.getAdapter();
                    if (adapter != null) {
                        MessageType messageType = MessageType.valueOf(lastMessage.get("messageType"));
                        MessageStatus messageStatus = MessageStatus.valueOf(lastMessage.get("messageStatus"));
                        String content = lastMessage.get("content");
                        String sentBy = lastMessage.get("sentBy");
                        String time = lastMessage.get("time");

                        Message message = new Message(content, time, sentBy, messageStatus, messageType);
                        adapter.addNewMessage(message);

                        if (sentBy.equals(UserManager.getCurrentUser().getIdentifier())) {
                            recyclerViewMessage.smoothScrollToPosition(adapter.getItemCount());
                        } else {
                            if(recyclerViewMessage.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerViewMessage.getLayoutManager();
                                if((messagesList.size() - 1) == (linearLayoutManager.findLastCompletelyVisibleItemPosition() + 2)) {
                                    recyclerViewMessage.smoothScrollToPosition(adapter.getItemCount());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadChatInformation() {
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

                    Message message = new Message(content, time, sentBy, messageStatus, messageType);
                    messageList.add(message);
                }

                buildMessageRecyclerView(messageList);
            }
        });
    }

    public void buildMessageRecyclerView(List<Message> messageList) {
        MessageAdapter messageAdapter = new MessageAdapter(messageList, ChatActivity.this);
        recyclerViewMessage.setAdapter((messageAdapter));
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
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
                        syncFriendInformation(identifier);
                        break;
                    }
                }
            }
        });
    }

    public void syncFriendInformation(String friendIdentifier) {
        FireDatabase.getDataBaseReferenceWithPath("users").child(friendIdentifier).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot user) {
                String contactName = (String) user.child("displayName").getValue();
                String imgExtension = (String) user.child("imgExtension").getValue();
                String imgKey = (String) user.child("imgKey").getValue();
                ConnectionStatus connectionStatus = ConnectionStatus.valueOf(user.child("connectionStatus").getValue().toString());

                txtContactName.setText(contactName);

                decorateConnectionStatus(connectionStatus);
                fetchFriendProfilePicture(imgExtension, imgKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            txtContactStatus.setAnimation(down_anim);
            firstAnimationRun = false;
        } else {
            if(!firstAnimationRun) {
                up_anim = AnimationUtils.loadAnimation(ChatActivity.this, R.anim.slide_to_right_movement);
                txtContactStatus.setVisibility(View.INVISIBLE);
                txtContactStatus.setAnimation(up_anim);
            }

            firstAnimationRun = false;
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
