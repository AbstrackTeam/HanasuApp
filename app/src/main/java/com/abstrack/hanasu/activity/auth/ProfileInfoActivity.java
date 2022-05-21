package com.abstrack.hanasu.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.R;

public class ProfileInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
    }

    public void changeToLastActivity(View view) {
        finish();
    }
}