package com.abstrack.hanasu.util;

import android.content.Intent;
import android.view.Window;
import com.abstrack.hanasu.BaseAppActivity;

import java.util.Objects;

public class AndroidUtil {

    public static void startNewActivity(BaseAppActivity currentActivity, Class nextActivityClass){
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }

    public static void hideActionBar(BaseAppActivity baseAppActivity){
        baseAppActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(baseAppActivity.getSupportActionBar()).hide();
    }

}