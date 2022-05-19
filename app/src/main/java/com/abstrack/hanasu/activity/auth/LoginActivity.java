package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.activity.LandingActivity;
import com.abstrack.hanasu.activity.SetUsernameActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.util.Preferences;
import com.abstrack.hanasu.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

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
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()) {
                            Util.startNewActivity(LoginActivity.this, VerifyEmailActivity.class);
                            return;
                        }

                        DatabaseReference databaseReference = Util.getFbDatabase().getReference();
                        databaseReference.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                boolean hasIdentifier = false;

                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                    return;
                                }
                                for (DataSnapshot user : task.getResult().getChildren()) {
                                    if (!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())) {
                                        continue;
                                    }
                                    if (user.child("identifier").getValue() != null) {
                                        Preferences.setIdentifier(user.child("identifier").getValue().toString(), LoginActivity.this);
                                        hasIdentifier = true;
                                        break;
                                    }
                                }
                                if(hasIdentifier) {
                                    Util.startNewActivity(LoginActivity.this, LandingActivity.class);
                                }
                                else{
                                    Util.startNewActivity(LoginActivity.this, SetUsernameActivity.class);
                                }
                            }
                        });


                    }
                });
    }

    public void changeToRegisterActivity(View view) {
        Util.startNewActivity(this, RegisterActivity.class);
    }

    public void changeToForgotPasswordActivity(View view){
        Util.startNewActivity(this, ForgotPasswordActivity.class);
    }

}