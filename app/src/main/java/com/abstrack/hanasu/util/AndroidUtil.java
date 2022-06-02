package com.abstrack.hanasu.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.abstrack.hanasu.BaseAppActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class AndroidUtil {

    public static void chooseGalleryPhoto(BaseAppActivity currentActivity, int pickCode){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select a Profile Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        currentActivity.startActivityForResult(chooserIntent, pickCode);
    }

    public static String getCurrentHour(){
        Calendar rightNow = Calendar.getInstance();
        String messageAmPm = "PM";

        if(rightNow.get(Calendar.AM_PM) == Calendar.AM){
            messageAmPm = "AM";
        }

        return rightNow.get(Calendar.HOUR) + ":" + rightNow.get(Calendar.MINUTE) + " " + messageAmPm;
    }

    public static String takeCameraPhoto(BaseAppActivity currentActivity, int captureCode) {
        if(ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(currentActivity, new String[] {Manifest.permission.CAMERA}, captureCode);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(currentActivity.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = ImageUtil.createImageFile();
                } catch (IOException ex) {
                    Log.d("HanasuFile", "An error ocurred while creating a file", ex);
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(currentActivity,
                            "com.abstrack.hanasu.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    currentActivity.startActivityForResult(takePictureIntent, captureCode);
                    return photoFile.getAbsolutePath();
                }
            }
        }
        return null;
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