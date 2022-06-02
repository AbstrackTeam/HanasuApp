package com.abstrack.hanasu.activity.landing;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.chat.ChatsAdapter;
import com.abstrack.hanasu.core.story.StoriesAdapter;
import com.abstrack.hanasu.core.story.Story;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.notification.NotificationBuilder;
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

    private CardView showMoreButton, addChatButton, addGroupsButton, searchButton;
    private ImageView showMoreButtonIcon;
    private boolean showingMoreOptions;

    private List<Story> stories = new ArrayList<>();
    private static List<Chat> chats = new ArrayList<Chat>();

    UserServiceThread userService = new UserServiceThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();
        load();
    }

    private void init() {
        NotificationBuilder.createNotificationChannel(this);
        userService.start();

        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

        showMoreButton = findViewById(R.id.showMoreOptions);
        addChatButton = findViewById(R.id.addFriend);
        addGroupsButton = findViewById(R.id.addGroup);
        searchButton = findViewById(R.id.search);

        showingMoreOptions = false;

        showMoreButtonIcon = findViewById(R.id.moreOptionsIcon);

        addChatButton.setVisibility(View.GONE);
        addGroupsButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);

        // Testing purposes - WILL REFACTOR
        // Stories
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));


        StoriesAdapter storiesAdapter = new StoriesAdapter(stories, storiesBar, this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        storiesBar.setItemAnimator(null);

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
    }

    public void startAddFriendActivity() {
        AndroidUtil.startNewActivity(LandingActivity.this, AddFriendActivity.class);
    }

    public void load(){
        DatabaseReference currentUserRef = FireDatabase.getDataBaseReferenceWithPath("users").child(UserManager.getCurrentUser().getIdentifier()).child("contacts");
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.w("hanasu-landing", "Loading landing contacts");

                // Load chats by contacts
                HashMap<String, String> contacts = UserManager.getCurrentUser().getContacts();

                List<String> keys = new ArrayList<>(contacts.keySet());

                // Clear all the chats
                chats.clear();
                chatsListView.removeAllViews();

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
                            ;

                            ArrayList<String> users = (ArrayList<String>) result.child("users").getValue();

                            String userIdentifier = "";

                            for (String currentUser : users) {
                                if (!currentUser.equals(UserManager.getCurrentUser().getIdentifier())) {
                                    userIdentifier = currentUser;
                                }
                            }

                            List<HashMap<String, String>> messagesList = (List<HashMap<String, String>>) result.child("messagesList").getValue();
                            int messageCount = 0;



                            String sentBy = messagesList.get(messagesList.size() - 1).get("sentBy");

                            /*
                                For getting the messageQuantity, you have to know if you did see the last message
                                and if you were the one who sended the message
                             */

                            if(!sentBy.equals("")) {
                                // If you didn't send the message.
                                if (!sentBy.equals(UserManager.getCurrentUser().getIdentifier())) {
                                    for(int i = 1; i < messagesList.size(); i++){
                                        // then we are going to count all the messages that don't have the tag "SEEN"
                                        if(messagesList.get(i).get("sentBy").equals(UserManager.getCurrentUser().getIdentifier())){
                                            continue;
                                        }
                                        if (MessageStatus.valueOf(messagesList.get(i).get("messageStatus")) != MessageStatus.SEEN)  {
                                            messageCount += 1;
                                        }
                                    }
                                }
                            }

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

    private void addToChats(Chat chat) {
        chats.add(chat);

        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, LandingActivity.this);
        chatsListView.setAdapter((chatsAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(LandingActivity.this, RecyclerView.VERTICAL, false));
    }

    public void animateOptions() {
        if (!showingMoreOptions) {
            showMoreButtonIcon.setImageResource(R.drawable.ic_down_arrow);

            showMoreButton.setClickable(false);
            addChatButton.setClickable(false);
            addGroupsButton.setClickable(false);
            searchButton.setClickable(false);

            showMoreButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMoreButton.setClickable(true);
                    addChatButton.setClickable(true);
                    addGroupsButton.setClickable(true);
                    searchButton.setClickable(true);
                    showingMoreOptions = true;
                }
            }, 450);

            addChatButton.setVisibility(View.VISIBLE);
            addGroupsButton.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);

            addChatButton.setAlpha(0.5f);
            addChatButton.setTranslationY(0);
            addChatButton.setTranslationY(showMoreButton.getY() - addChatButton.getY());
            addChatButton.animate()
                    .alpha(1.0f).
                    translationY(0)
                    .setDuration(300);

            addGroupsButton.setAlpha(0.5f);
            addGroupsButton.setTranslationY(0);
            addGroupsButton.setTranslationY(showMoreButton.getY() - addGroupsButton.getY());
            addGroupsButton.animate()
                    .alpha(1.0f)
                    .translationY(0)
                    .setDuration(300);

            searchButton.setAlpha(0.5f);
            searchButton.setTranslationY(0);
            searchButton.setTranslationY(showMoreButton.getY() - searchButton.getY());
            searchButton.animate()
                    .alpha(1.0f)
                    .translationY(0)
                    .setDuration(300);
            return;
        }
        showMoreButton.setClickable(false);
        addChatButton.setClickable(false);
        addGroupsButton.setClickable(false);
        searchButton.setClickable(false);
        showMoreButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                addChatButton.setVisibility(View.GONE);
                addGroupsButton.setVisibility(View.GONE);
                searchButton.setVisibility(View.GONE);
                showMoreButton.setClickable(true);
                addChatButton.setClickable(true);
                addGroupsButton.setClickable(true);
                searchButton.setClickable(true);
                showingMoreOptions = false;
            }
        }, 450);

        showMoreButtonIcon.setImageResource(R.drawable.ic_plus);
        addChatButton.animate()
                .translationY(showMoreButton.getY() - addChatButton.getY())
                .setDuration(300);
        addGroupsButton.animate().
                translationY(showMoreButton.getY() - addGroupsButton.getY())
                .setDuration(300);
        searchButton.animate().
                alpha(0.0f).
                setDuration(0);
        searchButton.animate()
                .alpha(0.0f).
                translationY(showMoreButton.getY() - searchButton.getY())
                .setDuration(300);
    }
}