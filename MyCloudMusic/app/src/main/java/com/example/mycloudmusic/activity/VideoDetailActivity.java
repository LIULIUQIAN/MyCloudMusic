package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mycloudmusic.R;

public class VideoDetailActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Log.e("VideoDetailActivity",extraId());
    }
}
