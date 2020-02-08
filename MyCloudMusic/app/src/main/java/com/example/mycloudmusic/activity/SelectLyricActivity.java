package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.util.Constant;

public class SelectLyricActivity extends BaseTitleActivity {

    private Song data;

    public static void start(Activity activity, Song data) {

        Intent intent = new Intent(activity, SelectLyricActivity.class);
        intent.putExtra(Constant.DATA, data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lyric);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        data = (Song) getIntent().getSerializableExtra(Constant.DATA);
        Log.e("data", "歌曲名称===" + data.getTitle());
    }

}
