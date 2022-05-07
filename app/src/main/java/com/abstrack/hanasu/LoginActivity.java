package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.abstrack.hanasu.util.AndroidUtil;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtil.hideActionBar(this);
        setContentView(R.layout.activity_login);
    }
}