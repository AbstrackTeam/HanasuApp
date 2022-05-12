package com.abstrack.hanasu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abstrack.hanasu.util.AndroidUtil;
import com.google.android.material.textfield.TextInputLayout;

public class VerifyActivity extends BaseAppActivity {

    TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        textViewEmail = findViewById(R.id.txtEmail);
        textViewEmail.setText(AuthManager.getFireAuth().getCurrentUser().getEmail());

        AuthManager.getFireAuth().signOut();
    }

    public void changeToLoginActivity(View view) {
        AndroidUtil.startNewActivity(this, LoginActivity.class);
    }
}