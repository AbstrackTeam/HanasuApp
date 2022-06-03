package com.abstrack.hanasu.activity;

import android.os.Bundle;
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
import com.abstrack.hanasu.core.Preferences;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.db.FireDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class LoadingActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

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

    public void manageActivity(){
        if(!AuthManager.isUserLogged()){
            goToLoginActivity();
            return;
        }

        if(!AndroidUtil.isNetworkConnected(this)){
            goToLandingActivity();
            return;
        }

        manageNextActivity();
    }

    public void goToLoginActivity(){
        AuthManager.getFireAuth().signOut();
        AndroidUtil.startNewActivity(LoadingActivity.this, LoginActivity.class);
    }

    public void goToLandingActivity() {
        AndroidUtil.startNewActivity(LoadingActivity.this, LandingActivity.class);
    }

    public void manageNextActivity(){
        String cachedUserIdentifier = Preferences.getValue("userCachedIdentifier", this);

        if(!cachedUserIdentifier.isEmpty()){
            fetchUserInformation(cachedUserIdentifier);
        }

        goToLoginActivity();
    }

    public void fetchUserInformation(String cachedUserIdentifier){
        FireDatabase.getDataBaseReferenceWithPath("users").child(cachedUserIdentifier).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean hasDisplayName = false;

                if (!task.isSuccessful()) {
                    Log.e("Hanasu-Loading", "Error getting data", task.getException());
                    return;
                }

                String displayName = task.getResult().child("public").child("displayName").getValue().toString();

                if(AuthManager.getFireAuth().getCurrentUser().isEmailVerified()) {
                    if(!displayName.isEmpty()) {
                        goToLandingActivity();
                    }

                    goToSetInfoActivity();
                }

                goToLoginActivity();
            }
        });
    }

    public void goToSetInfoActivity(){
        AndroidUtil.startNewActivity(this, SetProfileInfoActivity.class);
    }
}
