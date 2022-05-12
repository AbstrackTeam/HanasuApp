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

        AuthManager.getFireAuth().signOut();

        if(AuthManager.isUserLogged()){
            if(!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()){
                AndroidUtil.startNewActivity(this, LoginActivity.class);
                return;
            }

            AndroidUtil.startNewActivity(this, LandingActivity.class);
            return;
        }

        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}