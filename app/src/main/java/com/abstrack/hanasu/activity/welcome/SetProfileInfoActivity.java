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
import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.core.Flame;
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

    private static final int IMAGE_CAPTURE_CODE = 1999, IMAGE_PICK_CODE = 2000;

    private boolean showPictureOptions = false, progressFirstRun = false;
    private String tempCameraImagePath = "";

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        clickableContainer = findViewById(R.id.clickableContainer);
        clickableContainer.setClickable(false);
        clickableContainer.setVisibility(View.INVISIBLE);

        LinearLayout layoutPictureOptions = findViewById(R.id.layoutPictureOptions);

        viewPictureOptions = LayoutInflater.from(this).inflate(R.layout.select_picture_option, null, false);
        viewPictureOptions.setVisibility(View.INVISIBLE);

        layoutPictureOptions.addView(viewPictureOptions);

        edtTxtProfileName = findViewById(R.id.edtTxtProfileName);
        btnContinue = findViewById(R.id.btnContinue);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tempCameraImagePath = AndroidUtil.takeCameraPhoto(this, IMAGE_CAPTURE_CODE);
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void fetchCameraPhoto() {
        File f = new File(tempCameraImagePath);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri imageUri = Uri.fromFile(f);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);

        uploadPhoto(imageUri, ".jpg");
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

    public void fetchProfilePic(Uri imgUri, String imgExtension) {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);

        if (imgExtension.equals(".gif")) {
            Glide.with(this).asGif().load(imgUri).into(profilePicImgView);
        } else {
            Glide.with(this).asBitmap().load(imgUri).into(profilePicImgView);
        }
    }

    public void uploadByBytes(byte[] bytes, Uri imgUri, String imgKey, String imgExtension){
        StorageReference imageStorageReference = Flame.getStorageReference().child("image").child(imgKey + imgExtension);
        imageStorageReference.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                if(!progressFirstRun) {
                    updatePictureOptions(findViewById(android.R.id.content).getRootView());
                    progressFirstRun = true;
                }
            }
        });
    }

    public void  uploadByFile(Uri imgUri, String imgKey, String imgExtension) {
        StorageReference imageStorageReference = Flame.getStorageReference().child("image").child(imgKey + imgExtension);
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
                if(!progressFirstRun) {
                    updatePictureOptions(findViewById(android.R.id.content).getRootView());
                    progressFirstRun = true;
                }
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void removeProfilePicture(View view) {
        ImageView profilePicImgView = findViewById(R.id.imgProfile);
        profilePicImgView.setImageBitmap(ImageUtil.convertDrawableToBitmap(getDrawable(R.drawable.ic_profile_pic)));
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

    public void submit(View view) {
        if (!AuthManager.validateTextField(edtTxtProfileName)) {
            return;
        }

        UserManager.updateUserData("displayName", edtTxtProfileName.getText().toString());
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void openCamera(View view) {
        tempCameraImagePath = AndroidUtil.takeCameraPhoto(this, IMAGE_CAPTURE_CODE);
    }

    public void openGallery(View view) {
        AndroidUtil.chooseGalleryPhoto(this, IMAGE_PICK_CODE);
    }
}