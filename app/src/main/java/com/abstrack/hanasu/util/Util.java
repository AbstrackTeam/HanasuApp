package com.abstrack.hanasu.util;

import android.content.Intent;
import android.view.Window;
import com.abstrack.hanasu.BaseAppActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Util {

    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    public static void startNewActivity(BaseAppActivity currentActivity, Class nextActivityClass){
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }

    public static void hideActionBar(BaseAppActivity baseAppActivity){
        baseAppActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(baseAppActivity.getSupportActionBar()).hide();
    }

    public static FirebaseDatabase getDatabaseReference(){
        return FirebaseDatabase.getInstance();
    }

}