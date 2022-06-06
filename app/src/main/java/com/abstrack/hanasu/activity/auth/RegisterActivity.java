package com.abstrack.hanasu.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.TextUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends BaseAppActivity {

    private TextInputLayout emailInputText, passwordInputText, confirmPasswordInputText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInputText = findViewById(R.id.textInputLayoutEmail);
        passwordInputText = findViewById(R.id.textInputLayoutPswd);
        confirmPasswordInputText = findViewById(R.id.textInputLayoutPswdC);

        Button btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        TextView txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });
    }

    public void createAccount() {
        if (!TextUtil.validateRegisterForm(emailInputText, passwordInputText, confirmPasswordInputText))
            return;

        String emailText = emailInputText.getEditText().getText().toString();
        String passwordText = passwordInputText.getEditText().getText().toString();

        Flame.getFireAuth().createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-Register", "Error on Register", task.getException());
                    return;
                }

                goToVerifyEmailActivity();
            }
        });
    }

    public void goToVerifyEmailActivity(){
        AndroidUtil.startNewActivity(this, VerifyEmailActivity.class);
    }

    public void goToLoginActivity() {
        AndroidUtil.startNewActivity(RegisterActivity.this, LoginActivity.class);
    }
}