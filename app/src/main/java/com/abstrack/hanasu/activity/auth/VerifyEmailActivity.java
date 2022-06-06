package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class VerifyEmailActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        TextView txtViewEmail = findViewById(R.id.txtViewEmail);
        txtViewEmail.setText(Flame.getFireAuth().getCurrentUser().getEmail());

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        TextView txtResend = findViewById(R.id.txtResend);
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationEmail();
            }
        });

        sendVerificationEmail();
    }

    public void sendVerificationEmail() {
        Flame.getFireAuth().getCurrentUser().sendEmailVerification();
    }

    public void goToLoginActivity() {
        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}