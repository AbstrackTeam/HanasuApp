package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abstrack.hanasu.util.AndroidUtil;

public class LoginActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
    }
    public void changeToRegisterActivity(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}