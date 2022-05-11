package com.abstrack.hanasu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
    }

    public void sendForgotPasswordCode(View view){
        if(!AuthManager.validateEmailText(emailTextInput))
            return;

        String emailText = emailTextInput.getEditText().getText().toString();

        AuthManager.getFireAuth().sendPasswordResetEmail(emailText)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    public void changeToLastActivity(View view) {
        finish();
    }
}