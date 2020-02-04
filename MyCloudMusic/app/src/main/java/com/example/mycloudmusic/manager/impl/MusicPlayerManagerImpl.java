package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.manager.MusicPlayerManager;

import java.io.IOException;

public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl instance;
    private final Context context;
    private final MediaPlayer player;
    private Song data;

    private MusicPlayerManagerImpl(Context context) {
        this.context = context;

        player = new MediaPlayer();
    }

    public static synchronized MusicPlayerManagerImpl getInstance(Context context) {

        if (instance == null) {
            instance = new MusicPlayerManagerImpl(context);
        }
        return instance;
    }


    @Override
    public void play(String uri, Song data) {

        this.data = data;
        try {

            //释放播放器
            player.reset();
            player.setDataSource(uri);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {

        if (isPlaying()) {
            player.pause();
        }
    }

    @Override
    public void resume() {

        if (!isPlaying()) {
            player.start();
        }
    }
}
