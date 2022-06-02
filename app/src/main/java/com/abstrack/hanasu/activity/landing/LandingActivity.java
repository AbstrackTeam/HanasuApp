package com.abstrack.hanasu.activity.landing;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.chat.Chat;
import com.abstrack.hanasu.core.chat.ChatsAdapter;
import com.abstrack.hanasu.core.story.StoriesAdapter;
import com.abstrack.hanasu.core.story.Story;
import com.abstrack.hanasu.core.user.UserManager;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends BaseAppActivity {

    private RecyclerView storiesBar, chatsListView;
    private CardView showMoreButton, addChatButton, addGroupsButton, searchButton;
    private ImageView showMoreButtonIcon;
    private boolean showingMoreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();
    }

    private void init() {
        UserManager.fetchInitialUserData();

        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

        showMoreButton = findViewById(R.id.showMoreOptions);
        addChatButton = findViewById(R.id.addChat);
        addGroupsButton = findViewById(R.id.addGroup);
        searchButton = findViewById(R.id.search);

        showingMoreOptions = false;

        showMoreButtonIcon = findViewById(R.id.moreOptionsIcon);

        addChatButton.setVisibility(View.GONE);
        addGroupsButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        List<Story> stories = new ArrayList<>();
        List<Chat> chats = new ArrayList<>();

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

        // Chats
        chats.add(new Chat(false, "Bochin", 0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Soy una persona insaciable", 0, 4, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));

        StoriesAdapter storiesAdapter = new StoriesAdapter(stories, storiesBar, this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        storiesBar.setItemAnimator(null);

        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, this);
        chatsListView.setAdapter((chatsAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        addChatButton = findViewById(R.id.addChat);

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
                addFriend();
            }
        });
    }

    public void addFriend() {
        // TODO
        Toast.makeText(this, "add chat button clicked", Toast.LENGTH_SHORT).show();
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