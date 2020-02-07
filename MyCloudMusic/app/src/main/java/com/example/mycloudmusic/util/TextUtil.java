package com.example.mycloudmusic.util;

import android.graphics.Paint;

public class TextUtil {

    public static float getTextWidth(Paint paint, String data) {

        return paint.measureText(data);

    }

    public static float getTextHeight(Paint paint) {

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (float) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
    }
}
