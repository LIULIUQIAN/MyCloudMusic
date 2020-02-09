package com.example.mycloudmusic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewUtil {
    public static Bitmap captureBitmap(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Drawable background = view.getBackground();

        if (background != null){
            background.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);

        return bitmap;
    }
}
