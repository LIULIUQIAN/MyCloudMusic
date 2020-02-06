package com.example.mycloudmusic.util;

import android.content.Context;

public class DensityUtil {

    public static int dipTopx(Context context, float data) {

        float scale = context.getResources().getDisplayMetrics().density;

        return (int) (data * scale + 0.5f);
    }
}
