package com.example.mycloudmusic.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtil {
    public static boolean saveToFile(Bitmap bitmap, File file) {

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }
}
