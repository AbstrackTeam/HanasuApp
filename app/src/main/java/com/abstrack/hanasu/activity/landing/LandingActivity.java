package com.abstrack.hanasu.activity.landing;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.chat.ChatsAdapter;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.thread.UserServiceThread;
import com.abstrack.hanasu.core.chatroom.message.data.MessageStatus;
import com.abstrack.hanasu.db.FireDatabase;
import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LandingActivity extends BaseAppActivity {

    private static RecyclerView storiesBar, chatsListView;

    private CardView showMoreButton, addChatButton, addGroupsButton, searchButton, showUserInfo;
    private ImageView showMoreButtonIcon, noChatsImageView;
    private TextView noChatsTextView;
    private boolean showingMoreOptions;

    private static List<Chat> chats = new ArrayList<Chat>();

    UserServiceThread userService = new UserServiceThread();

    private Animation fromBottomAnim, toBottomAnim, fromRightAnim, toRightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();
        load();
    }

    private void init() {
        userService.start();

        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

        showMoreButton = findViewById(R.id.showMoreOptions);
        addChatButton = findViewById(R.id.addFriend);
        addGroupsButton = findViewById(R.id.addGroup);
        searchButton = findViewById(R.id.search);
        showUserInfo = findViewById(R.id.showUserInfo);

        noChatsImageView = findViewById(R.id.noChatsImageView);
        noChatsTextView = findViewById(R.id.noChatsTextView);

        showingMoreOptions = false;

        showMoreButtonIcon = findViewById(R.id.moreOptionsIcon);

        addChatButton.setVisibility(View.GONE);
        addGroupsButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        showUserInfo.setVisibility(View.GONE);

        fromBottomAnim = AnimationUtils.loadAnimation(this, R.anim.options_up);
        toBottomAnim = AnimationUtils.loadAnimation(this, R.anim.options_down);
        toRightAnim = AnimationUtils.loadAnimation(this, R.anim.options_right);
        fromRightAnim = AnimationUtils.loadAnimation(this, R.anim.options_left);

        addChatButton = findViewById(R.id.addFriend);

        buildOptionsListeners();
    }

    public void buildOptionsListeners() {
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateOptions();
            }
        });

        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddFriendActivity();
            }
        });

        showUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserInfoActivity();
            }
        });
    }

    public void startAddFriendActivity() {
        AndroidUtil.startNewActivity(LandingActivity.this, AddFriendActivity.class);
    }

    public void load(){
        checkAndUpdateNoChatsIcon();
        DatabaseReference currentUserRef = FireDatabase.getDataBaseReferenceWithPath("users").child(UserManager.getCurrentUser().getIdentifier()).child("contacts");
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("hanasu-landing", "Loading landing contacts");

                // Load chats by contacts
                HashMap<String, String> contacts = UserManager.getCurrentUser().getContacts();
                System.out.println("Contacts: " + contacts);

                List<String> keys = new ArrayList<>(contacts.keySet());

                // Clear all the chats
                chats.clear();
                chatsListView.removeAllViews();
                checkAndUpdateNoChatsIcon();

                // Get the chat room
                for (String identifier : keys){
                    String chatRoom = contacts.get(identifier);

                    // it will try to get the information with firebase
                    DatabaseReference chatRoomRef = FireDatabase.getDataBaseReferenceWithPath("chat-rooms").child(chatRoom);

                    chatRoomRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            DataSnapshot result = task.getResult();

                            if (result.getValue() == null) {
                                return;
                            }

                            ArrayList<String> users = (ArrayList<String>) result.child("users").getValue();

                            String userIdentifier = "";

                            for (String currentUser : users) {
                                if (!currentUser.equals(UserManager.getCurrentUser().getIdentifier())) {
                                    userIdentifier = currentUser;
                                }
                            }

                            List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) result.child("messagesList").getValue();
                            int messageCount = 0;

                            MessageStatus messageState = MessageStatus.valueOf(messagesList.get(messagesList.size() - 1).get("messageStatus"));

                            String lastMessage = messagesList.get(messagesList.size() -1).get("content");
                            String time = messagesList.get(messagesList.size() -1).get("time");
                            DatabaseReference userRef = FireDatabase.getDataBaseReferenceWithPath("users").child(userIdentifier);

                            int finalMessageCount = messageCount;

                            userRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        return;
                                    }
                                    String userIdentifier = task.getResult().child("identifier").getValue(String.class);
                                    String name = task.getResult().child("displayName").getValue(String.class);
                                    String imgKey = task.getResult().child("imgKey").getValue(String.class);
                                    String imgExtension = task.getResult().child("imgExtension").getValue(String.class);

                                    // Finally, add a new chat
                                    addToChats(new Chat(name, messageState, finalMessageCount, lastMessage, time, chatRoom, userIdentifier, imgKey, imgExtension));
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showUserInfoActivity(){
        AndroidUtil.startNewActivity(this, UserInfoActivity.class);
    }

    private void addToChats(Chat chat) {
        chats.add(chat);
        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, LandingActivity.this);
        chatsListView.setAdapter((chatsAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(LandingActivity.this, RecyclerView.VERTICAL, false));
        checkAndUpdateNoChatsIcon();
    }

    private void checkAndUpdateNoChatsIcon() {
       if (chats.size() > 0) {
            noChatsTextView.setVisibility(View.GONE);
            noChatsImageView.setVisibility(View.GONE);
            return;
        }
        noChatsTextView.setVisibility(View.VISIBLE);
        noChatsImageView.setVisibility(View.VISIBLE);
    }

    private void animateOptions() {
        if (!showingMoreOptions) {
            startOptionsShowAnimation();
            setOptionsClickable(true);
            showingMoreOptions = true;
            return;
        }
        setOptionsClickable(false);
        startOptionsCloseAnimation();
        showingMoreOptions = false;
    }

    private void setOptionsClickable(boolean bool) {
        addChatButton.setClickable(bool);
        addGroupsButton.setClickable(bool);
        searchButton.setClickable(bool);
        showUserInfo.setClickable(bool);
    }

    private void startOptionsShowAnimation() {
        showMoreButtonIcon.setImageResource(R.drawable.ic_down_arrow);

        setOptionsVisibility(View.VISIBLE);

        addChatButton.startAnimation(fromBottomAnim);
        addGroupsButton.startAnimation(fromBottomAnim);
        searchButton.startAnimation(fromBottomAnim);
        showUserInfo.startAnimation(fromRightAnim);
    }

    private void startOptionsCloseAnimation() {
        showMoreButtonIcon.setImageResource(R.drawable.ic_plus);

        addChatButton.startAnimation(toBottomAnim);
        addGroupsButton.startAnimation(toBottomAnim);
        searchButton.startAnimation(toBottomAnim);
        showUserInfo.startAnimation(toRightAnim);

        setOptionsVisibility(View.GONE);
    }

    private void setOptionsVisibility(int visibility) {
        addChatButton.setVisibility(visibility);
        addGroupsButton.setVisibility(visibility);
        searchButton.setVisibility(visibility);
        showUserInfo.setVisibility(visibility);
    }
}