package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.chatroom.chat.Chat;
import com.abstrack.hanasu.core.chatroom.chat.ChatsAdapter;
import com.abstrack.hanasu.core.chatroom.message.data.MessageStatus;
import com.abstrack.hanasu.core.story.StoriesAdapter;
import com.abstrack.hanasu.core.story.Story;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.thread.ChatThread;
import com.abstrack.hanasu.thread.UserThread;
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

    private UserThread userThread;
    private ChatThread chatThread;

    private List<Story> storiesList = new ArrayList<Story>();
    private List<Chat> chatsList = new ArrayList<Chat>();

    private boolean showingMoreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        init();
        buildOptionsListeners();
        addStoriesToView();
    }

    public void init() {
        userThread = new UserThread(this);
        userThread.start();

        chatThread = new ChatThread(this);
        chatThread.start();

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


    public void addChatsToView(){
        ChatsAdapter chatsAdapter = new ChatsAdapter(chatsList, LandingActivity.this);
        chatsListView.setAdapter((chatsAdapter));
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