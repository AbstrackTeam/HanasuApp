package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class VerifyForgottenPasswordActivity extends BaseAppActivity {

    public static String textEmail;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_forgotten_password);

        textViewEmail = findViewById(R.id.txtEmail);
        textViewEmail.setText(textEmail);
    }

    public void sendPasswordEmail(View view) {
        AuthManager.getFireAuth().sendPasswordResetEmail(textEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }


    public void changeToLastActivity(View view) { finish(); }
}