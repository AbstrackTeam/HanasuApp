package com.abstrack.hanasu.activity.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.Util;
import com.abstrack.hanasu.activity.landing.LandingActivity;

import jp.wasabeef.blurry.Blurry;

public class ProfileInfoActivity extends BaseAppActivity {

    private ConstraintLayout container;
    private LinearLayout cameraOptionLayout, clickableLayout;
    private View pictureOptionView;
    private boolean showPictureOptions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        container = findViewById(R.id.container);
        clickableLayout = findViewById(R.id.clickableContainer);

        cameraOptionLayout = findViewById(R.id.cameraOptionsLayout);
        pictureOptionView = LayoutInflater.from(this).inflate(R.layout.select_profile_picture,null, false);
        pictureOptionView.setVisibility(View.GONE);
        cameraOptionLayout.addView(pictureOptionView);
    }

    @Override
    public void onBackPressed() {
        if(showPictureOptions){
            clickableLayout.setClickable(false);
            Blurry.delete((ViewGroup) findViewById(R.id.container));
            pictureOptionView.setVisibility(View.GONE);
            showPictureOptions = false;
            return;
        }

        Util.startNewActivity(ProfileInfoActivity.this, LandingActivity.class);
    }

    public void openCameraOptions(View view) {
        if(showPictureOptions) {
            clickableLayout.setClickable(false);
            Blurry.delete((ViewGroup) findViewById(R.id.container));
            pictureOptionView.setVisibility(View.GONE);
            showPictureOptions = false;
        } else {
            clickableLayout.setClickable(true);
            Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.container));
            pictureOptionView.setVisibility(View.VISIBLE);
            showPictureOptions = true;
        }
    }

    //TODO
    public void submit(View view){
        Util.startNewActivity(this, LandingActivity.class);
    }
}