package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycloudmusic.R;

import java.time.Instant;

public class MusicPlayerActivity extends AppCompatActivity {

    public static void start(Activity activity){

        Intent intent = new Intent(activity,MusicPlayerActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }
}
