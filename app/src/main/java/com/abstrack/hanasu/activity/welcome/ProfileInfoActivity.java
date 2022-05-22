package com.abstrack.hanasu.activity.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.Util;
import com.abstrack.hanasu.activity.landing.LandingActivity;

public class ProfileInfoActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
    }

    public void submit(View view){
        Util.startNewActivity(this, LandingActivity.class);
    }

    public void changeToLastActivity(View view) {
        finish();
    }
}