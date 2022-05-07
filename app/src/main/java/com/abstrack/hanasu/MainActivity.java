package com.abstrack.hanasu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abstrack.hanasu.util.AndroidUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidUtil.hideActionBar(this);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}