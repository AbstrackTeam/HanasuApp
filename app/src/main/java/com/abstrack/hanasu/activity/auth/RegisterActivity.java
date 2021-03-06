package com.abstrack.hanasu.activity.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.AndroidUtil;
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
    }

    public void createAccount(View view) {
        if (!AuthManager.validateRegisterForm(emailInputText, passwordInputText, confirmPasswordInputText))
            return;

        String emailText = emailInputText.getEditText().getText().toString();
        String passwordText = passwordInputText.getEditText().getText().toString();

        AuthManager.getFireAuth().createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("HanasuFirebase", "Error RegisterIn", task.getException());
                            return;
                        }

                        AndroidUtil.startNewActivity(RegisterActivity.this, VerifyEmailActivity.class);
                    }
                });
    }

    public void changeToLoginActivity(View view) {
        AndroidUtil.startNewActivity(RegisterActivity.this, LoginActivity.class);
    }
}