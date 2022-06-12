package com.abstrack.hanasu.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.abstrack.hanasu.BaseAppActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtil {

    public static String getMimeType(Context context, Uri uriImage) {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[]{MediaStore.MediaColumns.MIME_TYPE},
                null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            strMimeType = cursor.getString(0);
        }

        strMimeType = strMimeType.replace("image/", "");
        strMimeType = "." + strMimeType;
        return strMimeType;
    }

    public static Bitmap convertPictureDataToBitmap(Intent data) {
        if (data == null) {
            return null;
        }

        Bundle extras = data.getExtras();
        return (Bitmap) extras.get("data");
    }

    public static File createImageFile(BaseAppActivity currentActivity) throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = currentActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
    }


    private String getFileExt(Context ctx, Uri contentUri) {
        ContentResolver c = ctx.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertUriToBitmap(Context ctx, Uri uri){
        Bitmap bitmap = null;
        ContentResolver contentResolver = ctx.getContentResolver();
        try {
            if(Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
