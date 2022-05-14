package com.abstrack.hanasu;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.abstrack.hanasu.logic.chat.Chat;
import com.abstrack.hanasu.logic.chat.ChatsAdapter;
import com.abstrack.hanasu.logic.story.StoriesAdapter;
import com.abstrack.hanasu.logic.story.Story;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends BaseAppActivity {

    private RecyclerView storiesBar, chatsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        init();
    }

    private void init(){
        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

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
        chats.add(new Chat(false, "Bochin",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Soy una persona insaciable",0, 4, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));

        StoriesAdapter storiesAdapter = new StoriesAdapter(stories, storiesBar,this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        storiesBar.setItemAnimator(null);

        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, this);
        chatsListView.setAdapter((chatsAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        CardView addChatButton = findViewById(R.id.addChatButton);
        addChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChat();
            }
        });
    }

    public void addChat(){
        // TODO
        Toast.makeText(this, "add chat button clicked", Toast.LENGTH_SHORT).show();
    }
}