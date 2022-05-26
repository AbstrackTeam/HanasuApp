package com.abstrack.hanasu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abstrack.hanasu.util.AndroidUtil;

public class BaseAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtil.hideActionBar(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
