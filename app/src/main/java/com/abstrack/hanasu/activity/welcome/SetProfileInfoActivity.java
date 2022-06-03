package com.abstrack.hanasu.activity.welcome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.core.Flame;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.util.ImageUtil;
import com.abstrack.hanasu.util.TextUtil;
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

    private String tempCameraImagePath = "";
    private boolean showPictureOptions = false, progressFirstRun = false;

    private static final int IMAGE_CAPTURE_CODE = 1999, IMAGE_PICK_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_info);

        init();
        buildProfilePictureOptions();
        setOnClickListeners();
    }

    public void init(){
        clickableContainer = findViewById(R.id.clickableContainer);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);

        edtTxtProfileName = findViewById(R.id.edtTxtProfileName);
        btnContinue = findViewById(R.id.btnContinue);
    }

    public void buildProfilePictureOptions(){
        LinearLayout layoutPictureOptions = findViewById(R.id.layoutPictureOptions);
        viewPictureOptions = LayoutInflater.from(this).inflate(R.layout.select_picture_option, null, false);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        layoutPictureOptions.addView(viewPictureOptions);
    }

    public void setOnClickListeners(){
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

    public void submit() {
        if (!TextUtil.validateTextField(edtTxtProfileName)) {
            return;
        }

        UserManager.updateUserData("public", "displayName", edtTxtProfileName.getText().toString());
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void openGallery() {
        AndroidUtil.chooseGalleryPhoto(this, IMAGE_PICK_CODE);
    }

    public void openCamera() {
        tempCameraImagePath = AndroidUtil.openCameraAndTakePhoto(this, IMAGE_CAPTURE_CODE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void removeProfilePicture() {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);
        profilePicImgView.setImageBitmap(ImageUtil.convertDrawableToBitmap(getDrawable(R.drawable.ic_profile_pic)));
        hidePictureOptions();
    }

    public void updatePictureOptions() {
        if (showPictureOptions) {
            hidePictureOptions();
            return;
        }

        showPictureOptions();
    }

    private void showPictureOptions() {
        btnContinue.setClickable(false);
        clickableContainer.setClickable(true);
        clickableContainer.setVisibility(View.VISIBLE);
        viewPictureOptions.setVisibility(View.VISIBLE);
        showPictureOptions = true;
    }

    private void hidePictureOptions() {
        btnContinue.setClickable(true);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        showPictureOptions = false;
    }

    @Override
    public void onBackPressed() {
        if (showPictureOptions) {
            hidePictureOptions();
            return;
        }

        if (!TextUtil.validateTextField(edtTxtProfileName)) {
            return;
        }

        AndroidUtil.startNewActivity(SetProfileInfoActivity.this, LandingActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_CAPTURE_CODE:
                    uploadPhoto(AndroidUtil.getPhotoUriViaFile(this, tempCameraImagePath), ".jpg");
                    break;
                case IMAGE_PICK_CODE:
                    uploadPhoto(data.getData(), ImageUtil.getMimeType(this, data.getData()));
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadPhoto(Uri imgUri, String imgExtension) {
        String imgKey = UUID.randomUUID().toString();

        if(imgExtension.equals(".gif")){
            uploadByFile(imgUri, imgKey, imgExtension);
            return;
        }
        // Compress and reduce image weight
        byte[] compressedImgBytes = ImageUtil.compressImage(imgExtension, imgUri, getContentResolver());
        uploadByBytes(compressedImgBytes, imgUri, imgKey, imgExtension);
    }

    public void  uploadByFile(Uri imgUri, String imgKey, String imgExtension) {
        StorageReference imageStorageReference = Flame.getStorageReference().child("image").child(imgKey + imgExtension);
        imageStorageReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Hanasu-SetProfileInfo", "Image uploaded successfully");
                UserManager.updateUserData("public","imgKey", imgKey + imgExtension);

                updateProfilePicImgView(imgUri, imgExtension);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Hanasu-SetProfileInfo", "Image failed to upload", e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                if(!progressFirstRun) {
                    updatePictureOptions();
                    progressFirstRun = true;
                }
            }
        });
    }

    public void uploadByBytes(byte[] bytes, Uri imgUri, String imgKey, String imgExtension){
        StorageReference imageStorageReference = Flame.getStorageReference().child("image").child(imgKey + imgExtension);
        imageStorageReference.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Hanasu-SetProfileInfo", "Image uploaded successfully");
                UserManager.updateUserData("public","imgKey", imgKey + imgExtension);

                updateProfilePicImgView(imgUri, imgExtension);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Hanasu-SetProfileInfo", "Image failed to upload", e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                if(!progressFirstRun) {
                    updatePictureOptions();
                    progressFirstRun = true;
                }
            }
        });
    }

    public void updateProfilePicImgView(Uri imgUri, String imgExtension) {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);

        if (imgExtension.equals(".gif")) {
            Glide.with(this).asGif().load(imgUri).into(profilePicImgView);
        } else {
            Glide.with(this).asBitmap().load(imgUri).into(profilePicImgView);
        }
    }
}