package com.abstrack.hanasu.activity.landing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.AndroidUtil;

public class AddFriendActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();
    }

    public void init(){
        Button returnToLandingButton = findViewById(R.id.returnToLandingButton);
        returnToLandingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtil.startNewActivity(AddFriendActivity.this, LandingActivity.class);
            }
        });
    }
}