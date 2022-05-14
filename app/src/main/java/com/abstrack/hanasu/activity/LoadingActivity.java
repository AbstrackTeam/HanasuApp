package com.abstrack.hanasu.activity;

import android.os.Bundle;
import android.util.Log;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.auth.LoginActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.util.Util;

public class LoadingActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(AuthManager.isUserLogged()){
            if(!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()){
                AuthManager.getFireAuth().signOut();
                Util.startNewActivity(this, LoginActivity.class);
                return;
            }

            Util.startNewActivity(this, LandingActivity.class);
            return;
        }

        Util.startNewActivity(this, LoginActivity.class);
        return;
    }
}