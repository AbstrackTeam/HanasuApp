package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.AndroidUtil;

public class VerifyEmailActivity extends BaseAppActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        TextView txtViewEmail = findViewById(R.id.txtViewEmail);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            txtViewEmail.setText(extras.getString("userCachedEmail"));
        }

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