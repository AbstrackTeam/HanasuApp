package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.abstrack.hanasu.logic.chat.Chat;
import com.abstrack.hanasu.logic.chat.ChatsAdapter;
import com.abstrack.hanasu.logic.story.StoriesAdapter;
import com.abstrack.hanasu.logic.story.StoriesDecorator;
import com.abstrack.hanasu.logic.story.Story;
import com.abstrack.hanasu.util.AndroidUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LandingActivity extends BaseAppActivity {

    private RecyclerView storiesBar, chatsListView;

    @Override
    public void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState, R.layout.activity_landing);
        init();
    }

    private void init(){
        storiesBar = findViewById(R.id.storiesBar);
        chatsListView = findViewById(R.id.chatsListView);

        List<Story> stories = new ArrayList<>();
        List<Chat> chats = new ArrayList<>();

        // Testing purposes
        // Stories
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(false));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        stories.add(new Story(true));
        // Chats
        chats.add(new Chat(false, "Bochin",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Soy una persona insaciable",0, 4, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bredo mejor juego",0, 7, "EL VATOO XDDDDDD", "2:34 AM"));
        chats.add(new Chat(false, "hola",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "OMA NENO",0, 1, "marciano", "2:34 AM"));
        chats.add(new Chat(false, "Bochin5",0, 7, "WEY", "2:34 AM"));
        chats.add(new Chat(false, "Bochin6",0, 45, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin7",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin8",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin9",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin10",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin11",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin11",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "Bochin12231",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "sd",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "sd",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "sd",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));
        chats.add(new Chat(false, "sd",0, 7, "Hola amigo emoticón \uD83D\uDE00 desgraciado", "2:34 AM"));

        StoriesAdapter storiesAdapter = new StoriesAdapter(stories, this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        // Space between story items
        storiesBar.addItemDecoration(new StoriesDecorator(10));

        ChatsAdapter chatsAdapter = new ChatsAdapter(chats, this);
        chatsListView.setAdapter((chatsAdapter));
        chatsListView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }
}