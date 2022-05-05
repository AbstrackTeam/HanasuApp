package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.abstrack.hanasu.util.StoriesAdapter;
import com.abstrack.hanasu.util.StoriesDecorator;
import com.abstrack.hanasu.util.Story;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LandingScreen extends AppCompatActivity {

    private RecyclerView storiesBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
    }

    private void init(){
        storiesBar = findViewById(R.id.storiesBar);

        List<Story> stories = new ArrayList<>();
        // Testing purposes
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

        StoriesAdapter storiesAdapter = new StoriesAdapter(stories, this);
        storiesBar.setAdapter(storiesAdapter);
        storiesBar.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        // Space between story items
        storiesBar.addItemDecoration(new StoriesDecorator(10));

    }
}