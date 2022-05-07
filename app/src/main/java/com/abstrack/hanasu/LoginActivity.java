package com.abstrack.hanasu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.util.AndroidUtil;

public class LoginActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void changeToRegisterActivity(View view) {
        AndroidUtil.startNewActivity(this, RegisterActivity.class);
    }
}