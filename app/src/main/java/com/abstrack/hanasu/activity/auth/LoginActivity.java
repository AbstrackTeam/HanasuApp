package com.abstrack.hanasu.activity.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.activity.welcome.WelcomeActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.db.FireDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;

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
        if(!AuthManager.validateLoginForm(emailInputText, passwordInputText))
            return;

        String emailText = emailInputText.getEditText().getText().toString();
        String passwordText = passwordInputText.getEditText().getText().toString();

        AuthManager.getFireAuth().signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.e("HanasuFirebase", "Error Login In", task.getException());
                    return;
                }

                if(!AuthManager.getFireAuth().getCurrentUser().isEmailVerified()){
                    AuthManager.getFireAuth().signOut();
                    AndroidUtil.startNewActivity(LoginActivity.this, VerifyEmailActivity.class);
                    return;
                }

                FireDB.getFbDatabase().getReference().child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        boolean hasIdentifier = false;

                        if (!task.isSuccessful()) {
                            Log.e("HanasuFirebase", "Error getting data", task.getException());
                            return;
                        }

                        for(DataSnapshot user : task.getResult().getChildren()){
                            if(!user.child("uid").getValue().equals(AuthManager.getFireAuth().getCurrentUser().getUid())){
                                continue;
                            }

                            if(user.child("identifier").getValue() != null) {
                                UserManager.createCurrentUser(user.child("name").getValue().toString(), user.child("tag").getValue().toString());
                                hasIdentifier = true;
                                break;
                            }
                        }

                        if(hasIdentifier){
                            AndroidUtil.startNewActivity(LoginActivity.this, LandingActivity.class);
                            return;
                        }

                        AndroidUtil.startNewActivity(LoginActivity.this, WelcomeActivity.class);
                        return;
                    }
                });
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