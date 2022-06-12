package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.callback.OnChatDataReceiveCallback;
import com.abstrack.hanasu.callback.OnChatRoomDataReceiveCallback;
import com.abstrack.hanasu.callback.OnUserDataReceiveCallback;
import com.abstrack.hanasu.core.chatroom.ChatRoom;
import com.abstrack.hanasu.core.chatroom.ChatRoomManager;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.chat.ChatAdapter;
import com.abstrack.hanasu.core.chatroom.chat.ChatManager;
import com.abstrack.hanasu.core.contact.ContactManager;
import com.abstrack.hanasu.core.story.StoriesAdapter;
import com.abstrack.hanasu.core.story.Story;
import com.abstrack.hanasu.core.user.PrivateUser;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LandingActivity extends BaseAppActivity {

    private static RecyclerView chatsListView;
    private CardView showMoreButton, addChatButton, addGroupsButton, searchButton;
    private ImageView showMoreButtonIcon, noChatsImageView;
    private TextView noChatsTextView;
    private Animation fromBottomAnim, toBottomAnim;

    private boolean showingMoreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        init();
        buildOptionsListeners();
        fetchInitialData(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchInitialData(true);
    }

    public void init() {
        UserManager.initInitialValues();
        chatsListView = findViewById(R.id.chatsListView);

        showMoreButton = findViewById(R.id.showMoreOptions);
        addChatButton = findViewById(R.id.addFriend);
        addGroupsButton = findViewById(R.id.addGroup);
        searchButton = findViewById(R.id.search);

        showMoreButtonIcon = findViewById(R.id.moreOptionsIcon);

        noChatsImageView = findViewById(R.id.noChatsImageView);
        noChatsTextView = findViewById(R.id.noChatsTextView);

        addChatButton.setVisibility(View.GONE);
        addGroupsButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);

        fromBottomAnim = AnimationUtils.loadAnimation(this, R.anim.options_up);
        toBottomAnim = AnimationUtils.loadAnimation(this, R.anim.options_down);

        showingMoreOptions = false;
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
                goToAddFriendActivity();
            }
        });
    }

    public void fetchInitialData(boolean resumed) {

        if(resumed){
            ContactManager.getContactPublicUserList().clear();
            ChatManager.getChatsList().clear();
        }

        UserManager.fetchPublicAndPrivateData(new OnUserDataReceiveCallback() {
            @Override
            public void onDataReceiver(PublicUser publicUser) {
            }

            @Override
            public void onDataReceiver(PrivateUser privateUser) {
                ContactManager.fetchPublicData(new OnUserDataReceiveCallback() {
                    @Override
                    public void onDataReceiver(PublicUser publicUser) {

                    }

                    @Override
                    public void onDataReceiver(PrivateUser privateUser) {
                    }

                    @Override
                    public void onDataReceived() {
                        ChatRoomManager.fetchPrivateData(new OnChatRoomDataReceiveCallback() {
                            @Override
                            public void onDataReceiver(ChatRoom chatRoom) {
                                new Chat().createChat(chatRoom);
                            }

                            @Override
                            public void onDataReceived() {
                                addChatsToView();
                                buildDataListeners();
                                checkAndUpdateNoChatsIcon();
                            }
                        });
                    }
                });
            }

            @Override
            public void onDataReceived() {
                checkAndUpdateNoChatsIcon();
            }
        });
    }

    public void buildDataListeners() {
        UserManager.syncPublicAndPrivateData(new OnUserDataReceiveCallback() {
            @Override
            public void onDataReceiver(PublicUser publicUser) {
            }

            @Override
            public void onDataReceiver(PrivateUser privateUser) {

            }

            @Override
            public void onDataReceived() {
            }
        });

        ContactManager.syncPublicData(new OnUserDataReceiveCallback() {
            @Override
            public void onDataReceiver(PublicUser publicUser) {
            }

            @Override
            public void onDataReceiver(PrivateUser privateUser) {
            }

            @Override
            public void onDataReceived() {
                checkAndUpdateNoChatsIcon();
            }
        });
    }

    public void goToAddFriendActivity() {
        AndroidUtil.startNewActivity(LandingActivity.this, AddFriendActivity.class);
    }

    public void addChatsToView() {
        ChatAdapter chatAdapter = new ChatAdapter(ChatManager.getChatsList(), this);
        chatsListView.setAdapter((chatAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(LandingActivity.this, RecyclerView.VERTICAL, false));
    }

    private void checkAndUpdateNoChatsIcon() {
        HashMap<String, PublicUser> hola = ContactManager.getContactPublicUserList();

        if (hola.size() > 0) {
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
    }

    private void startOptionsShowAnimation() {
        showMoreButtonIcon.setImageResource(R.drawable.ic_down_arrow);

        setOptionsVisibility(View.VISIBLE);

        addChatButton.startAnimation(fromBottomAnim);
        addGroupsButton.startAnimation(fromBottomAnim);
        searchButton.startAnimation(fromBottomAnim);
    }

    private void startOptionsCloseAnimation() {
        showMoreButtonIcon.setImageResource(R.drawable.ic_plus);

        addChatButton.startAnimation(toBottomAnim);
        addGroupsButton.startAnimation(toBottomAnim);
        searchButton.startAnimation(toBottomAnim);

        setOptionsVisibility(View.GONE);
    }

    private void setOptionsVisibility(int visibility) {
        addChatButton.setVisibility(visibility);
        addGroupsButton.setVisibility(visibility);
        searchButton.setVisibility(visibility);
    }
}