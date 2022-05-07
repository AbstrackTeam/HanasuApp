package com.abstrack.hanasu.util;

import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AndroidUtil {

    public static void hideActionBar(AppCompatActivity appCompatActivity){
        appCompatActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(appCompatActivity.getSupportActionBar()).hide();
    }

}