package com.abstrack.hanasu.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.auth.LoginActivity;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.activity.welcome.SetProfileInfoActivity;
import com.abstrack.hanasu.activity.welcome.WelcomeActivity;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.notification.MessageNotifier;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.ImageUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.IOException;

public class LoadingActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Flame.setDataPersistence();
        MessageNotifier.createNotificationChannel(this);

        animateContent();
        manageActivity();

    }

    public void animateContent() {
        TextView txtViewAppName = findViewById(R.id.txtViewAppName);
        ImageView imgViewMainLogo = findViewById(R.id.imgViewMainLogo);

        Animation down_anim = AnimationUtils.loadAnimation(this, R.anim.down_movement);

        txtViewAppName.setAnimation(down_anim);
        imgViewMainLogo.setAnimation(down_anim);
    }

    public void manageActivity() {
        if (!Flame.isFireUserLogged()) {
            goToLoginActivity();
            return;
        }

        if (!AndroidUtil.isNetworkConnected(this)) {
            goToLandingActivity();
            return;
        }

        manageNextActivity();
    }

    public void goToLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
            }
        }, 3000);

    }

    public void goToLandingActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtil.startNewActivity(LoadingActivity.this, LandingActivity.class);
            }
        }, 3000);

    }

    public void goToWelcomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtil.startNewActivity(LoadingActivity.this, WelcomeActivity.class);
            }
        }, 3000);
    }

    public void goToSetProfileInfoActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AndroidUtil.startNewActivity(LoadingActivity.this, SetProfileInfoActivity.class);
            }
        }, 3000);
    }

    public void manageNextActivity() {
        changeActivityViaData();
    }

    public void changeActivityViaData() {
        Flame.getDataBaseReferenceWithPath("public").child("users").child(Flame.getFireAuth().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-Loading", "Error getting data", task.getException());
                    return;
                }

                if (!Flame.getFireAuth().getCurrentUser().isEmailVerified()) {
                    goToLoginActivity();
                    return;
                }

                if (task.getResult().child("identifier").getValue() == null) {
                    goToWelcomeActivity();
                    return;
                }

                String displayName = task.getResult().child("displayName").getValue(String.class);

                if (displayName != null && !displayName.isEmpty()) {
                    goToLandingActivity();
                    return;
                }

                goToSetProfileInfoActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                goToLoginActivity();
            }
        });
    }
}
