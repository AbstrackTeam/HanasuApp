package com.abstrack.hanasu.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtil {

    public static String getMimeType(Context context, Uri uriImage) {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[]{MediaStore.MediaColumns.MIME_TYPE},
                null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }

    public static Bitmap convertPictureDataToBitmap(Intent data) {
        if (data == null) {
            return null;
        }

        Bundle extras = data.getExtras();
        Bitmap profilePicBitmap = (Bitmap) extras.get("data");
        return profilePicBitmap;
    }

    public static Bitmap convertPictureStreamToBitmap(Context ctx, Intent data) {
        try {
            InputStream inputStream = ctx.getContentResolver().openInputStream(data.getData());
            Bitmap profilePicBitmap = BitmapFactory.decodeStream(inputStream);
            return profilePicBitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
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

}
