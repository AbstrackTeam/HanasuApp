package com.abstrack.hanasu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.auth.LoginActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.util.Util;

public class LoadingActivity extends BaseAppActivity {

    private Animation down_anim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        animateContent();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                changeActivity();
            }

        }, 4000);
    }

    public void animateContent() {
        TextView txtHanasu = findViewById(R.id.txtHanasu);
        ImageView imgLogo = findViewById(R.id.imgLogo);

        down_anim = AnimationUtils.loadAnimation(this, R.anim.down_movement);

        imgLogo.setAnimation(down_anim);
        txtHanasu.setAnimation(down_anim);
    }

    public void changeActivity(){
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