package com.abstrack.hanasu.util;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.Window;
import com.abstrack.hanasu.BaseAppActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class AndroidUtil {

    public static void openGallery(BaseAppActivity currentActivity, int pickCode){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select a Profile Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        currentActivity.startActivityForResult(chooserIntent, pickCode);
    }

    public static void openCamera(BaseAppActivity currentActivity, int captureCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(currentActivity.getPackageManager()) != null) {
            currentActivity.startActivityForResult(takePictureIntent, captureCode);
        }
    }

    public static void startNewActivity(BaseAppActivity currentActivity, Class nextActivityClass){
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }

    public static void hideActionBar(BaseAppActivity baseAppActivity){
        baseAppActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(baseAppActivity.getSupportActionBar()).hide();
    }
}