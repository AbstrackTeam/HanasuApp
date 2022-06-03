package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;

public class VerifyForgottenPasswordActivity extends BaseAppActivity {

    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_forgotten_password);

        TextView textViewEmail = findViewById(R.id.txtViewEmail);
        extras = getIntent().getExtras();

        if (extras != null) {
            textViewEmail.setText(extras.getString("userCachedEmail"));
        }

        TextView txtResend = findViewById(R.id.txtResend);
        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordEmail();
            }
        });

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLastActivity();
            }
        });
    }

    public void sendPasswordEmail() {
        if (extras != null) {
            Flame.getFireAuth().sendPasswordResetEmail(extras.getString("userCachedEmail"));
        }
    }

    public void goToLastActivity() {
        finish();
    }
}