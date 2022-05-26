package com.abstrack.hanasu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.auth.LoginActivity;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class LoadingActivity extends BaseAppActivity {

    private Animation down_anim;
    boolean hasIdentifier = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        animateContent();
        load();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeActivity(hasIdentifier);
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

    public void load(){
        UserManager.fetchInitialUserData();
        FireDatabase.getFbDatabase().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                for(DataSnapshot user : task.getResult().getChildren()){
                    if(!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())){
                        continue;
                    }

                    if(user.child("identifier").getValue() != null){
                        hasIdentifier = true;
                        break;
                    }
                }
            }
        });
    }

    public void changeActivity(boolean hasIdentifier){
        if(!AuthManager.isUserLogged()){
            AndroidUtil.startNewActivity(this, LoginActivity.class);
            return;
        }

        if(hasIdentifier) {
            if (AuthManager.getFireAuth().getCurrentUser().isEmailVerified()) {
                AndroidUtil.startNewActivity(LoadingActivity.this, LandingActivity.class);
                return;
            }

            AuthManager.getFireAuth().signOut();
            AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
            return;
        }

        AuthManager.getFireAuth().signOut();
        AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
    }
}
