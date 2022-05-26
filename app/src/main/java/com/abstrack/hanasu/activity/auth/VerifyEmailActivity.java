package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class VerifyEmailActivity extends BaseAppActivity {

    TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        textViewEmail = findViewById(R.id.txtEmail);
        textViewEmail.setText(AuthManager.getFireAuth().getCurrentUser().getEmail());

        sendVerificationEmail(null);
    }

    public void sendVerificationEmail(View view){
        AuthManager.getFireAuth().getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            AuthManager.getFireAuth().signOut();
                        }
                    }
                });

    }

    public void changeToLoginActivity(View view) {
        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}