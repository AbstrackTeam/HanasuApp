package com.abstrack.hanasu.activity.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.callback.OnChatRoomDataReceiveCallback;
import com.abstrack.hanasu.callback.OnContactDataReceiveCallback;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.ChatRoomManager;
import com.abstrack.hanasu.core.chatroom.chat.ChatManager;
import com.abstrack.hanasu.core.chatroom.chat.message.Message;
import com.abstrack.hanasu.core.chatroom.chat.message.MessageAdapter;
import com.abstrack.hanasu.core.chatroom.chat.message.MessageManager;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageStatus;
import com.abstrack.hanasu.core.chatroom.chat.message.data.MessageType;
import com.abstrack.hanasu.core.chatroom.data.ChatType;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.user.data.ConnectionStatus;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class ChatActivity extends BaseAppActivity {

    private RecyclerView recyclerViewMessage;
    private TextView txtContactName, txtContactStatus;
    private ImageView imgContactProfilePicture;
    private EditText edtTxtMsg;
    private ChatRoom cachedChatRoom;
    private MessageAdapter messageAdapter;

    private Animation down_anim, up_anim;
    private boolean firstAnimationRun = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        buildButtonsActions();
        loadChatMessages(false);
        syncChatInformation();
    }

    public void init() {
        Intent intent = getIntent();
        cachedChatRoom = intent.getParcelableExtra("cachedChatRoom");

        recyclerViewMessage = findViewById(R.id.recyclerViewMessage);
        txtContactName = findViewById(R.id.txtContactName);
        txtContactStatus = findViewById(R.id.txtContactStatus);
        imgContactProfilePicture = findViewById(R.id.imgContactProfilePicture);
        edtTxtMsg = findViewById(R.id.edtTxtMsg);
    }

    public void buildButtonsActions() {
        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToLastActivity();
            }
        });
    }

    public void sendMessage() {
        if (!edtTxtMsg.getText().toString().isEmpty()) {
            //Send message via FCM
        }
    }

    public void syncChatInformation() {
        ChatRoomManager.syncPrivateDataByIdentifier(cachedChatRoom.getChatRoomUUID(), new OnChatRoomDataReceiveCallback() {
            @Override
            public void onDataReceiver(ChatRoom chatRoom) {
                cachedChatRoom = chatRoom;
                loadChatMessages(true);

                if(cachedChatRoom.getChatType() == ChatType.INDIVIDUAL){
                    for (String contactIdentifier : cachedChatRoom.getUsersList()) {
                        if (!contactIdentifier.equals(UserManager.currentPublicUser.getIdentifier())) {
                            UserManager.fetchContactPublicInformation(contactIdentifier, new OnContactDataReceiveCallback() {
                                @Override
                                public void onDataReceive(PublicUser contactPublicUser) {
                                    txtContactName.setText(contactPublicUser.getDisplayName());
                                    decorateConnectionStatus(contactPublicUser.getConnectionStatus());
                                    // WAITING FOR IMG RULES
                                }
                            });
                        }
                    }
                    return;
                }

                txtContactName.setText("Groupal chat");
                // Change status to Members List
            }
        });
    }

    public void loadChatMessages(boolean async) {
        if(!async) {
            MessageManager.getMessageList().clear();

            for (Message message : cachedChatRoom.getMessagesList()) {
                if(cachedChatRoom.getMessagesList().indexOf(message) > 0) {
                    MessageManager.addMessageToMessageList(message);
                }
            }

            buildMessageRecyclerView();
            return;
        }

        checkMessageListState();
    }

    public void checkMessageListState(){
        if(cachedChatRoom.getMessagesList().size() - 1 > MessageManager.getMessageList().size()) {
            for(Message message : cachedChatRoom.getMessagesList()) {
                if(cachedChatRoom.getMessagesList().indexOf(message) != cachedChatRoom.getMessagesList().size() - 1){
                    continue;
                }

                MessageManager.addMessageToMessageList(message);
                messageAdapter.notifyItemInserted(MessageManager.getMessageList().size() - 1);
                return;
            }
        }

        messageEdited();
    }

    public void messageEdited(){
        if(cachedChatRoom.getMessagesList().size() - 1 == MessageManager.getMessageList().size()){
            MessageManager.getMessageList().clear();

            for(Message message : cachedChatRoom.getMessagesList()) {
                if(cachedChatRoom.getMessagesList().indexOf(message) > 0) {
                    MessageManager.addMessageToMessageList(message);
                }
            }

            buildMessageRecyclerView();
            return;
        }

        messageDeleted();
    }

    public void messageDeleted() {
        if(cachedChatRoom.getMessagesList().size() - 1 < MessageManager.getMessageList().size()){
            for(Message message : MessageManager.getMessageList()){
                if(MessageManager.getMessageList().indexOf(message) != MessageManager.getMessageList().size() - 1){
                    continue;
                }

                MessageManager.removeMessageFromlist(message);
                messageAdapter.notifyItemRemoved(MessageManager.getMessageList().size());
                return;
            }
        }
    }


    public void decorateConnectionStatus(ConnectionStatus connectionStatus) {
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

    public void buildMessageRecyclerView() {
        messageAdapter = new MessageAdapter(MessageManager.getMessageList(), ChatActivity.this);
        recyclerViewMessage.setAdapter((messageAdapter));
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    public void returnToLastActivity() {
        finish();
    }
}
