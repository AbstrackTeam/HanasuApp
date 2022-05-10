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

        if (AuthManager.isUserLogged()) {
            AndroidUtil.startNewActivity(this, LandingActivity.class);
        } else {
            AndroidUtil.startNewActivity(this, LoginActivity.class);
        }
    }
}