package com.abstrack.hanasu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abstrack.hanasu.util.Util;

public class BaseAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.hideActionBar(this);
    }
}
