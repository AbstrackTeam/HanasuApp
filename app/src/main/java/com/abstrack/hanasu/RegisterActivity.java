package com.abstrack.hanasu;

import androidx.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseAppActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void changeToLastActivity(View view){
        finish();
    }
}