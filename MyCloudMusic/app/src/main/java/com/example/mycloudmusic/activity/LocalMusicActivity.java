package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;

import java.util.List;

public class LocalMusicActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        fetchData();
    }

    private void fetchData() {

        List<Song> songs = orm.queryLocalMusic(0);
        if (songs.size() > 0){

        }else {

            startActivity(ScanLocalMusicActivity.class);
        }
    }
}
