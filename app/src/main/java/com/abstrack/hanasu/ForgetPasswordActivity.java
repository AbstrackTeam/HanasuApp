package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.util.AndroidUtil;

public class ForgetPasswordActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }


    public void changeToLoginActivity(View view) {
        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}