package com.abstrack.hanasu.activity.welcome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

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

import java.io.File;
import java.util.UUID;

public class SetProfileInfoActivity extends BaseAppActivity {

    private LinearLayout clickableContainer;
    private View viewPictureOptions;

    private EditText edtTxtProfileName;
    private Button btnContinue;

    Animation hideOptionsAnim, showOptionsAnim, fadeInAnim, fadeOutAnim;

    private static final int IMAGE_CAPTURE_CODE = 1999, IMAGE_PICK_CODE = 2000, REQUEST_CAMERA_PERMISSIONS = 100;

    private boolean showPictureOptions = false;
    private String tempCameraImagePath = "";

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        clickableContainer = findViewById(R.id.clickableContainer);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);

        edtTxtProfileName = findViewById(R.id.edtTxtProfileName);
        btnContinue = findViewById(R.id.btnContinue);

        hideOptionsAnim = AnimationUtils.loadAnimation(this, R.anim.hide_profile_pic_options);
        showOptionsAnim = AnimationUtils.loadAnimation(this, R.anim.show_profile_pic_options);
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        viewPictureOptions = LayoutInflater.from(this).inflate(R.layout.select_picture_option, clickableContainer, false);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        clickableContainer.addView(viewPictureOptions);

        buildOptionsListeners();
    }

    private void buildOptionsListeners(){
        clickableContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePictureOptions();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        CardView crdVCamera = findViewById(R.id.crdVCamera);
        crdVCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePictureOptions();
            }
        });

        Button btnCamera = viewPictureOptions.findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });

        Button btnGallery = viewPictureOptions.findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Button btnTrash = viewPictureOptions.findViewById(R.id.btnTrash);
        btnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProfilePicture();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAPTURE_CODE:
                    fetchCameraPhoto();
                    break;
                case IMAGE_PICK_CODE:
                    uploadPhoto(data.getData(), ImageUtil.getMimeType(this, data.getData()));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void fetchCameraPhoto() {
        File f = new File(tempCameraImagePath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri imageUri = Uri.fromFile(f);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);

        uploadPhoto(imageUri, ".jpg");
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
                    updatePictureOptions();
                    progressFirstRun[0] = true;
                }
            }
        });
    }

    public void fetchProfilePic(Uri imgUri, String imgExtension) {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);

        if (imgExtension.equals(".gif")) {
            Glide.with(this).asGif().load(imgUri).into(profilePicImgView);
        } else {
            Glide.with(this).asBitmap().load(imgUri).into(profilePicImgView);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void removeProfilePicture() {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);
        profilePicImgView.setImageBitmap(ImageUtil.convertDrawableToBitmap(getDrawable(R.drawable.ic_profile_pic)));
        hidePictureOptions();
    }

    public void openCamera() {
        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_MEDIA,
                        Manifest.permission.MANAGE_DOCUMENTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tempCameraImagePath = AndroidUtil.openCameraAndTakePhoto(this, IMAGE_CAPTURE_CODE);
            } else {
                Toast.makeText(this, "Permiso denegado, no se podrá tomar foto", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void updatePictureOptions() {
        if (showPictureOptions) {
            hidePictureOptions();
            return;
        }

        showPictureOptions();
    }

    private void showPictureOptions() {
        startPictureOptionsShowAnimation();
        btnContinue.setClickable(false);
        clickableContainer.setClickable(true);
        clickableContainer.setVisibility(View.VISIBLE);
        viewPictureOptions.setVisibility(View.VISIBLE);
        showPictureOptions = true;
    }

    private void startPictureOptionsShowAnimation(){
        viewPictureOptions.startAnimation(showOptionsAnim);
        clickableContainer.startAnimation(fadeInAnim);
    }
    private void startPictureOptionsHideAnimation(){
        viewPictureOptions.startAnimation(hideOptionsAnim);
        clickableContainer.startAnimation(fadeOutAnim);
    }

    private void hidePictureOptions() {
        startPictureOptionsHideAnimation();
        btnContinue.setClickable(true);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        showPictureOptions = false;
    }

    public void submit() {
        if (!AuthManager.validateTextField(edtTxtProfileName)) {
            return;
        }

        UserManager.updateUserData("displayName", edtTxtProfileName.getText().toString());
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void openGallery() {
        AndroidUtil.chooseGalleryPhoto(this, IMAGE_PICK_CODE);
    }
}