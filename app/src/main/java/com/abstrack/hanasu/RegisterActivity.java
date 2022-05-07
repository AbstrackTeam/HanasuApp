package com.abstrack.hanasu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Bundle;

public class RegisterActivity extends BaseAppActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState, R.layout.activity_register);
    }
    public void changeToLoginActivity(View view){
        finish();
    }
}