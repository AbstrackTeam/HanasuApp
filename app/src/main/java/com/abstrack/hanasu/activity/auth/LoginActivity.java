package com.abstrack.hanasu.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.activity.welcome.SetProfileInfoActivity;
import com.abstrack.hanasu.activity.welcome.WelcomeActivity;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.TextUtil;
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

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAccount();
            }
        });

        TextView txtViewForgotPassword = findViewById(R.id.txtViewForgotPassword);
        txtViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToForgotPasswordActivity();
            }
        });

        TextView txtViewRegister = findViewById(R.id.txtViewRegister);
        txtViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
    }

    public void goToRegisterActivity() {
        AndroidUtil.startNewActivity(this, RegisterActivity.class);
    }

    public void goToForgotPasswordActivity() {
        Intent forgotPasswordActivityIntent = new Intent(this, ForgotPasswordActivity.class);
        forgotPasswordActivityIntent.putExtra("userCachedEmail", emailInputText.getEditText().getText().toString());
        startActivity(forgotPasswordActivityIntent);
    }

    public void loginAccount() {
        if (!TextUtil.validateLoginForm(emailInputText, passwordInputText))
            return;

        String emailText = emailInputText.getEditText().getText().toString();
        String passwordText = passwordInputText.getEditText().getText().toString();

        Flame.getFireAuth().signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-Login", "Error Login In", task.getException());
                    return;
                }

                changeActivityViaData();
            }
        });
    }

    public void changeActivityViaData() {
        Flame.getDataBaseReferenceWithPath("users").child(Flame.getFireAuth().getUid()).child("public").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Hanasu-Login", "Error getting data", task.getException());

                    if (!Flame.getFireAuth().getCurrentUser().isEmailVerified()) {
                        goToVerifyEmailActivity();
                    }
                    return;
                }

                if (Flame.getFireAuth().getCurrentUser().isEmailVerified()) {
                    if (task.getResult().child("displayName").getValue() != null) {
                        goToLandingActivity();
                    }

                    goToWelcomeActivity();
                    return;
                }

                goToVerifyEmailActivity();
            }
        });
    }

    public void goToLandingActivity() {
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void goToWelcomeActivity() {
        AndroidUtil.startNewActivity(this, WelcomeActivity.class);
    }

    public void goToVerifyEmailActivity() {
        AndroidUtil.startNewActivity(this, VerifyEmailActivity.class);
    }
}