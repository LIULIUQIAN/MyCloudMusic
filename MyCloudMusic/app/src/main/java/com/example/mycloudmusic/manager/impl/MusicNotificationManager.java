package com.example.mycloudmusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.NotificationUtil;

public class MusicNotificationManager implements MusicPlayerListener {

    private static MusicNotificationManager instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;
    private BroadcastReceiver musicNotificationBroadcastReceiver;

    private MusicNotificationManager(Context context) {
        this.context = context;

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);

        initMusicNotificationReceiver();
    }

    public static synchronized MusicNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicNotificationManager(context);
        }
        return instance;
    }

    /**
     * 初始化音乐通知广播接收器
     */
    private void initMusicNotificationReceiver() {
        musicNotificationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (Constant.ACTION_LIKE.equals(action)){
                    System.out.println("==========ACTION_LIKE");
                }else if (Constant.ACTION_PREVIOUS.equals(action)){
                    System.out.println("==========ACTION_PREVIOUS");
                }else if (Constant.ACTION_PLAY.equals(action)){
                    System.out.println("==========ACTION_PLAY");
                }else if (Constant.ACTION_NEXT.equals(action)){
                    System.out.println("==========ACTION_NEXT");
                }else if (Constant.ACTION_LYRIC.equals(action)){
                    System.out.println("==========ACTION_LYRIC");
                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_LIKE);
        intentFilter.addAction(Constant.ACTION_PREVIOUS);
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_LYRIC);
        context.registerReceiver(musicNotificationBroadcastReceiver,intentFilter);
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
