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
import com.abstrack.hanasu.util.Preferences;
import com.abstrack.hanasu.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class LoadingActivity extends BaseAppActivity {

    private Animation down_anim;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        animateContent();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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

    public void changeActivity() {
        if (!AuthManager.isUserLogged()) {
            Util.startNewActivity(LoadingActivity.this, LoginActivity.class);
            return;
        }
        if (!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()) {
            AuthManager.getFireAuth().signOut();
            Util.startNewActivity(LoadingActivity.this, LoginActivity.class);
            return;
        }

        DatabaseReference databaseReference = Util.getFbDatabase().getReference();
        databaseReference.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean hasIdentifier = false;
                String identifier = "";

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }
                for (DataSnapshot user : task.getResult().getChildren()) {
                    if (!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())) {
                        continue;
                    }
                    if (user.child("identifier").getValue() != null) {
                        Preferences.setIdentifier(user.child("identifier").getValue().toString(), LoadingActivity.this);
                        hasIdentifier = true;
                        identifier = user.child("identifier").getValue(String.class);
                        break;
                    }
                }
                if(hasIdentifier){
                    Util.startNewActivity(LoadingActivity.this, LandingActivity.class);
                    Preferences.setIdentifier(identifier, LoadingActivity.this);
                }
                else {
                    Util.startNewActivity(LoadingActivity.this, LoginActivity.class);
                }
            }
        });
    }
}
