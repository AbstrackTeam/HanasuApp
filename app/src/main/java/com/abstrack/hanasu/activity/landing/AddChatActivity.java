package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.Util;

public class AddChatActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        init();
    }

    public void init(){
        Button returnToLandingButton = findViewById(R.id.returnToLandingButton);
        returnToLandingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.startNewActivity(AddChatActivity.this, LandingActivity.class);
            }
        });
    }
}