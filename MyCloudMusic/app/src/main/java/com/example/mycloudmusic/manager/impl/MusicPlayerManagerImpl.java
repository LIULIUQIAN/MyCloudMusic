package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.Consumer;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl instance;
    private final Context context;
    private final MediaPlayer player;
    private Song data;

    List<MusicPlayerListener> listeners = new ArrayList<>();

    private MusicPlayerManagerImpl(Context context) {
        this.context = context;

        player = new MediaPlayer();


        initListeners();
    }

    /*
     * 设置播放器监听
     * */
    private void initListeners() {

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            /**
             * 播放器准备开始播放
             * 这里可以获取到音乐时长
             */
            @Override
            public void onPrepared(MediaPlayer mp) {
                data.setDuration(mp.getDuration());
                ListUtil.eachListener(listeners, listener -> listener.onPrepared(mp,data));
            }
        });
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

            ListUtil.eachListener(listeners, listener -> listener.onPlaying(data));

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

            ListUtil.eachListener(listeners, listener -> listener.onPaused(data));
        }
    }

    @Override
    public void resume() {

        if (!isPlaying()) {
            player.start();

            ListUtil.eachListener(listeners, listener -> listener.onPlaying(data));
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeMusicPlayerListener(MusicPlayerListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @Override
    public Song getData() {
        return data;
    }
}
