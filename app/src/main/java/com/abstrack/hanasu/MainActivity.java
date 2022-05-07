package com.abstrack.hanasu;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abstrack.hanasu.util.AndroidUtil;


public class MainActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}