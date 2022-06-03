package com.abstrack.hanasu.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends BaseAppActivity {

    private TextInputLayout emailTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailTextInput = findViewById(R.id.textInputLayoutEmail);

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        Button btnSend = findViewById(R.id.btnSend);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendForgotPasswordEmail();
            }
        });
    }

    public void sendForgotPasswordEmail() {
        if (!TextUtil.validateEmailText(emailTextInput))
            return;

        String emailText = emailTextInput.getEditText().getText().toString();

        Flame.getFireAuth().sendPasswordResetEmail(emailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            goToVerifyForgottenPasswordActivity();
                        }
                    }
                });
    }

    public void goToVerifyForgottenPasswordActivity(){
        Intent verifyEmailActivityIntent = new Intent(this, VerifyEmailActivity.class);
        verifyEmailActivityIntent.putExtra("userCachedEmail", Flame.getFireAuth().getCurrentUser().getEmail());

        Flame.getFireAuth().signOut();

        startActivity(verifyEmailActivityIntent);
    }

    public void goToLoginActivity() {
        AndroidUtil.startNewActivity(ForgotPasswordActivity.this, LoginActivity.class);
    }
}