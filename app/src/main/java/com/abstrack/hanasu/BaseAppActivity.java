package com.abstrack.hanasu;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.abstrack.hanasu.util.AndroidUtil;

public class BaseAppActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState, @LayoutRes int layoutResID) {
        super.onCreate(savedInstanceState);
        AndroidUtil.hideActionBar(this);
        setContentView(layoutResID);
    }

}
