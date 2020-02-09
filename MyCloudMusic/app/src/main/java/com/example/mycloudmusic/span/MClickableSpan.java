package com.example.mycloudmusic.span;

import android.media.audiofx.Visualizer;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import androidx.annotation.NonNull;

public abstract class MClickableSpan extends ClickableSpan {

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(ds.linkColor);
//        ds.setUnderlineText(true);
    }

}
