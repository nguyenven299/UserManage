package com.usermanage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import static com.usermanage.GetPathUri.getPath;

public class ConvertUriToBitmapChangedRotation {
    public Bitmap getBitmapFromUri(Uri uri, Context context) {
        Bitmap bitmapRotated = null;
        try {
            ExifInterface exif = new ExifInterface(getPath(context, uri));
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotationInDegrees != 0) {
                matrix.postRotate(rotationInDegrees);
            }
            Bitmap bitmap = BitmapFactory.decodeFile(getPath(context, uri));
            bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } catch (IOException e) {
            Log.e("WallOfLightApp", e.getMessage());
        }
        return bitmapRotated;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
