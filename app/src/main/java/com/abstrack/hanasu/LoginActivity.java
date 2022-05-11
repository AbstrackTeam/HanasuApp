package com.abstrack.hanasu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends BaseAppActivity {

    private TextInputLayout emailInputText, passwordInputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInputText = findViewById(R.id.textInputLayoutEmail);
        passwordInputText = findViewById(R.id.textInputLayoutPswd);
    }

    public void loginAccount(View view){
        if (!AuthManager.validateLoginForm(emailInputText, passwordInputText))
            return;

        String emailText = emailInputText.getEditText().getText().toString();
        String passwordText = passwordInputText.getEditText().getText().toString();

        AuthManager.getFireAuth().signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            AndroidUtil.startNewActivity(LoginActivity.this, LandingActivity.class);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void changeToRegisterActivity(View view) {
        AndroidUtil.startNewActivity(this, RegisterActivity.class);
    }
    public void changeToForgotPasswordActivity(View view){
        AndroidUtil.startNewActivity(this, ForgotPasswordActivity.class);
    }
}