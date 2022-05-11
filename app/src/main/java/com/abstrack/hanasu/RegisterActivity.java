package com.abstrack.hanasu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

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
                        if (task.isSuccessful()) {
                            AndroidUtil.startNewActivity(RegisterActivity.this, LandingActivity.class);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void changeToLastActivity(View view) {
        finish();
    }
}