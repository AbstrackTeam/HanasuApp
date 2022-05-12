package com.abstrack.hanasu;

import android.os.Bundle;
import com.abstrack.hanasu.util.AndroidUtil;

public class LoadingActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //For testing purposes
        AuthManager.getFireAuth().signOut();

        if(AuthManager.isUserLogged()){
            if(!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()){
                AuthManager.getFireAuth().signOut();
                AndroidUtil.startNewActivity(this, LoginActivity.class);
                return;
            }

            AndroidUtil.startNewActivity(this, LandingActivity.class);
            return;
        }

        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}