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
import com.abstrack.hanasu.activity.welcome.SetProfileInfoActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class LoadingActivity extends BaseAppActivity {

    private Animation down_anim;
    boolean hasIdentifier = false, hasDisplayName = false, hasValidUid = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        animateContent();
        load();
        changeActivity();
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
        FireDatabase.getDataBaseReferenceWithPath("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                for(DataSnapshot user : task.getResult().getChildren()){
                    if(user.child("uid").getValue() != null){
                        hasValidUid = true;
                    } else {
                        return;
                    }

                    if(!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())){
                        continue;
                    }

                    if(user.child("identifier").getValue() != null){
                        if(user.child("displayName").getValue() != null){
                            if(!user.child("displayName").getValue().equals("")){
                                hasDisplayName = true;
                            }
                        }

                        hasIdentifier = true;
                        break;
                    }
                }
            }
        });
    }

    public void changeActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!hasValidUid){
                    AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
                    return;
                }

                if(!AuthManager.isUserLogged()){
                    AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
                    return;
                }

                if(hasIdentifier) {
                    if (AuthManager.getFireAuth().getCurrentUser().isEmailVerified()) {
                        if(hasDisplayName){
                            AndroidUtil.startNewActivity(LoadingActivity.this, LandingActivity.class);
                            return;
                        }

                        AndroidUtil.startNewActivity(LoadingActivity.this, SetProfileInfoActivity.class);
                        return;
                    }

                    AuthManager.getFireAuth().signOut();
                    AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
                    return;
                }

                AuthManager.getFireAuth().signOut();
                AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
            }

        }, 3000);
    }
}
