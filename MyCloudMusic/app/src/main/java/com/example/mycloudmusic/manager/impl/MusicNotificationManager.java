package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.NotificationUtil;

public class MusicNotificationManager implements MusicPlayerListener {

    private static MusicNotificationManager instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;

    private MusicNotificationManager(Context context) {
        this.context = context;

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);
    }

    public static synchronized MusicNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicNotificationManager(context);
        }
        return instance;
    }

    @Override
    public void onPaused(Song data) {
        NotificationUtil.showMusicNotification(context, data, false);
    }

    @Override
    public void onPlaying(Song data) {
        NotificationUtil.showMusicNotification(context, data, true);
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

    }

    @Override
    public void onProgress(Song data) {

    }


}
