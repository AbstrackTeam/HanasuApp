package com.abstrack.hanasu.activity.welcome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.db.FireDatabase;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.ImageUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;

public class SetProfileInfoActivity extends BaseAppActivity {

    private LinearLayout layoutPictureOptions, clickableContainer;
    private View viewPictureOptions;

    private EditText edtTxtProfileName;

    private static final int IMAGE_CAPTURE_CODE = 1999, IMAGE_PICK_CODE = 2000;

    private boolean showPictureOptions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        clickableContainer = findViewById(R.id.clickableContainer);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);

        layoutPictureOptions = findViewById(R.id.layoutPictureOptions);

        viewPictureOptions = LayoutInflater.from(this).inflate(R.layout.select_picture_option, null, false);
        viewPictureOptions.setVisibility(View.INVISIBLE);

        layoutPictureOptions.addView(viewPictureOptions);

        edtTxtProfileName = findViewById(R.id.edtTxtProfileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAPTURE_CODE:
                    //Issue with camera
                case IMAGE_PICK_CODE:
                    uploadPhoto(data.getData(), ImageUtil.getMimeType(this, data.getData()));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadPhoto(Uri imgUri, String imgExtension){
        String imgKey = UUID.randomUUID().toString();
        final boolean[] progressFirstRun = {false};

        StorageReference imageStorageReference = FireDatabase.getStorageReference().child("image").child(imgKey + imgExtension);
        imageStorageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("HanasuStorage", "Image uploaded successfully");

                UserManager.updateUserData("imgKey", imgKey);
                UserManager.updateUserData("imgExtension", imgExtension);

                fetchProfilePic(imgUri, imgExtension);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("HanasuStorage", "Image failed to upload", e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                if(!progressFirstRun[0]) {
                    updatePictureOptions(findViewById(android.R.id.content).getRootView());
                    progressFirstRun[0] = true;
                }
            }
        });
    }

    public void fetchProfilePic(Uri imgUri, String imgExtension) {
        ImageView profilePicImgView = (ImageView) findViewById(R.id.imgProfile);

        if (imgExtension.equals(".gif")) {
            Glide.with(this).asGif().load(imgUri).into(profilePicImgView);
        } else {
            Glide.with(this).asBitmap().load(imgUri).into(profilePicImgView);
        }
    }

    public void removeProfilePicture(View view) {
        ImageView profilePicImgView = (ImageView) findViewById(R.id.imgProfile);
        profilePicImgView.setImageBitmap(ImageUtil.convertDrawableToBitmap(this.getDrawable(R.drawable.ic_profile_pic)));
        hidePictureOptions();
    }

    @Override
    public void onBackPressed() {
        if (showPictureOptions) {
            hidePictureOptions();
            return;
        }

        if (!AuthManager.validateTextField(edtTxtProfileName)) {
            return;
        }

        AndroidUtil.startNewActivity(SetProfileInfoActivity.this, LandingActivity.class);
    }

    public void updatePictureOptions(View view) {
        if (showPictureOptions) {
            hidePictureOptions();
            return;
        }

        showPictureOptions();
    }

    private void showPictureOptions() {
        clickableContainer.setClickable(true);
        clickableContainer.setVisibility(View.VISIBLE);
        viewPictureOptions.setVisibility(View.VISIBLE);
        showPictureOptions = true;
    }

    private void hidePictureOptions() {
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        showPictureOptions = false;
    }

    public void submit(View view) {
        if (!AuthManager.validateTextField(edtTxtProfileName)) {
            return;
        }

        UserManager.updateUserData("displayName", edtTxtProfileName.getText().toString());
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void openCamera(View view) {
        AndroidUtil.openCamera(this, IMAGE_CAPTURE_CODE);
    }

    public void openGallery(View view) {
        AndroidUtil.openGallery(this, IMAGE_PICK_CODE);
    }
}