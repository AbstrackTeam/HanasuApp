package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.abstrack.hanasu.util.AndroidUtil;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtil.hideActionBar(this);
        setContentView(R.layout.activity_register_screen);
    }
    public void changeToLoginActivity(View view){
        finish();
    }
}