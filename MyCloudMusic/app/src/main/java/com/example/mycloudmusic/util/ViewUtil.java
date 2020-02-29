package com.example.mycloudmusic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.activity.BaseCommonActivity;

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

    public static void initVerticalLinearRecyclerView(Context context, RecyclerView rv) {

        rv.setHasFixedSize(true);

        rv.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);
    }
}
