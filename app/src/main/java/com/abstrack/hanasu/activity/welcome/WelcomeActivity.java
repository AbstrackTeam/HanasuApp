package com.abstrack.hanasu.activity.welcome;

import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.Util;

public class WelcomeActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void changeToSetUsernameActivity(View view) {
        Util.startNewActivity(this, SetUsernameActivity.class);
    }
}