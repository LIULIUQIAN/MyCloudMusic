package com.example.mycloudmusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mycloudmusic.R;

public class GlobalLyricView extends LinearLayout {

    public GlobalLyricView(Context context) {
        super(context);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GlobalLyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        initViews();
    }

    private void initViews() {

        setBackgroundColor(getResources().getColor(R.color.global_lyric_background));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_global_lyric, this, false);
        addView(view);
    }
}
