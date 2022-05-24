package com.abstrack.hanasu.activity.welcome;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.abstrack.hanasu.BaseAppActivity;
import com.abstrack.hanasu.R;
import com.abstrack.hanasu.auth.AuthManager;
import com.abstrack.hanasu.core.user.UserManager;
import com.abstrack.hanasu.db.FireDB;
import com.abstrack.hanasu.util.AndroidUtil;
import com.abstrack.hanasu.activity.landing.LandingActivity;
import com.abstrack.hanasu.util.ImageUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import jp.wasabeef.blurry.Blurry;

public class SetProfileInfoActivity extends BaseAppActivity {

    private LinearLayout layoutPictureOptions, clickableContainer;
    private View viewPictureOptions;

    private ImageView profilePicImgView;

    private static final int IMAGE_CAPTURE_CODE = 1999, IMAGE_PICK_CODE = 2000;

    private boolean showPictureOptions = false;

    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        clickableContainer = findViewById(R.id.clickableContainer);
        clickableContainer.setClickable(false);

        layoutPictureOptions = findViewById(R.id.layoutPictureOptions);

        viewPictureOptions = LayoutInflater.from(this).inflate(R.layout.select_picture_option,null, false);
        viewPictureOptions.setVisibility(View.GONE);

        layoutPictureOptions.addView(viewPictureOptions);

        profilePicImgView = findViewById(R.id.imgProfile);
    }

    @Override
    public void onBackPressed() {
        if(showPictureOptions){
            hidePictureOptions();
            Blurry.delete((ViewGroup) findViewById(R.id.container));
            return;
        }

        AndroidUtil.startNewActivity(SetProfileInfoActivity.this, LandingActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            Blurry.delete((ViewGroup) findViewById(R.id.container));
            profilePicImgView.setImageBitmap(ImageUtil.convertPictureDataToBitmap(data));
            uploadProfilePicture(data.getData(), ImageUtil.getMimeType(this, data.getData()));

            Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.container));
            hidePictureOptions();
            Blurry.delete((ViewGroup) findViewById(R.id.container));
        } else if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            Blurry.delete((ViewGroup) findViewById(R.id.container));

            if(ImageUtil.getMimeType(this, data.getData()).equals(".gif")){
                Glide.with(this).asGif().load(data.getData()).into(profilePicImgView);
            } else {
                profilePicImgView.setImageBitmap(ImageUtil.convertPictureStreamToBitmap(this, data));
            }
            uploadProfilePicture(data.getData(), ImageUtil.getMimeType(this, data.getData()));

            Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.container));
            hidePictureOptions();
            Blurry.delete((ViewGroup) findViewById(R.id.container));
        }
    }

    //TODO
    public void submit(View view){
        AndroidUtil.startNewActivity(this, LandingActivity.class);
    }

    public void uploadProfilePicture(Uri profilePicUri, String imgExtension) {
        String imgKey = UUID.randomUUID().toString();

        StorageReference profilePicRef = FireDB.getStorageReference().child("image").child(imgKey + imgExtension);
        profilePicRef.putFile(profilePicUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("HanasuStorage", "Image uploaded successful");
                UserManager.uploadUserData("imgKey", imgKey);
                UserManager.uploadUserData("imgExtension", imgExtension);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("HanasuStorage", "Image failed to upload successfully", uploadTask.getException());
            }
        });
    }

    public void updatePictureOptions(View view) {
        if(showPictureOptions) {
            hidePictureOptions();
            Blurry.delete((ViewGroup) findViewById(R.id.container));
            return;
        }

        showPictureOptions();
        Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.container));
    }


    public void showPictureOptions(){
        clickableContainer.setClickable(true);
        viewPictureOptions.setVisibility(View.VISIBLE);
        showPictureOptions = true;
    }

    public void hidePictureOptions(){
        clickableContainer.setClickable(false);
        viewPictureOptions.setVisibility(View.INVISIBLE);
        showPictureOptions = false;
    }

    public void openCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE);
        }
    }

    public void openGallery(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select a Profile Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, IMAGE_PICK_CODE);
    }

    public void removeProfilePicture(View view){
        Blurry.delete((ViewGroup) findViewById(R.id.container));
        ImageView profilePicImgView = (ImageView) findViewById(R.id.imgProfile);
        profilePicImgView.setImageBitmap(ImageUtil.convertDrawableToBitmap(this.getDrawable(R.drawable.ic_profile_pic)));

        Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) findViewById(R.id.container));
        hidePictureOptions();
        Blurry.delete((ViewGroup) findViewById(R.id.container));

    }
}