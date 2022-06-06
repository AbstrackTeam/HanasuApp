package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import com.abstrack.hanasu.core.story.StoriesAdapter;
import com.abstrack.hanasu.core.story.Story;
import com.abstrack.hanasu.core.user.PrivateUser;
import com.abstrack.hanasu.core.user.PublicUser;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends BaseAppActivity {

    private static RecyclerView storiesBar, chatsListView;
    private CardView showMoreButton, addChatButton, addGroupsButton, searchButton;
    private ImageView showMoreButtonIcon;

    private List<Story> storiesList = new ArrayList<Story>();

    private boolean showingMoreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        init();
        buildOptionsListeners();
        syncLandingComponents();
    }

    public void init() {
        ChatManager.getChatsList().clear();

        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

        showMoreButton = findViewById(R.id.showMoreOptions);
        addChatButton = findViewById(R.id.addFriend);
        addGroupsButton = findViewById(R.id.addGroup);
        searchButton = findViewById(R.id.search);

        showMoreButtonIcon = findViewById(R.id.moreOptionsIcon);

        addChatButton.setVisibility(View.GONE);
        addGroupsButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);

        showingMoreOptions = false;
    }

    public void syncLandingComponents(){
        UserManager.syncPublicAndPrivateData(new OnUserDataReceiveCallback() {
            @Override
            public void onDataReceiver(PublicUser publicUser) {
            }

            @Override
            public void onDataReceiver(PrivateUser privateUser) {
                syncChatRoomData();
            }
        });
    }

    private void syncChatRoomData(){
        ChatRoomManager.syncPrivateData(new OnChatRoomDataReceiveCallback() {
            @Override
            public void onDataReceiver(ChatRoom chatRoom) {
                new Chat().retrieveChatData(chatRoom, LandingActivity.this, new OnChatDataReceiveCallback() {
                    @Override
                    public void onDataReceive(Chat chat) {
                        ChatManager.getChatsList().clear();
                        ChatManager.addChatToChatList(chat);
                        addChatsToView();
                    }
                });
            }
        });
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

    public void goToAddFriendActivity() {
        AndroidUtil.startNewActivity(LandingActivity.this, AddFriendActivity.class);
    }


    public void addChatsToView() {
        ChatAdapter chatAdapter = new ChatAdapter(ChatManager.getChatsList(), this);
        chatsListView.setAdapter((chatAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(LandingActivity.this, RecyclerView.VERTICAL, false));
    }

    public void addStoriesToView(){
        storiesList.add(new Story(false));

        StoriesAdapter storiesAdapter = new StoriesAdapter(storiesList, storiesBar, this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        storiesBar.setItemAnimator(null);
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