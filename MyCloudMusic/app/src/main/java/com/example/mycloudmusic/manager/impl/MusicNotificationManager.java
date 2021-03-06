package com.example.mycloudmusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.GlobalLyricManager;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.NotificationUtil;

public class MusicNotificationManager implements MusicPlayerListener {

    private static MusicNotificationManager instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;
    private final GlobalLyricManager globalLyricManager;
    private BroadcastReceiver musicNotificationBroadcastReceiver;
    private final ListManager listManager;

    private MusicNotificationManager(Context context) {
        this.context = context;

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);
        listManager = MusicPlayerService.getListManager(this.context);

        globalLyricManager = GlobalLyricManagerImpl.getInstance(this.context);

        initMusicNotificationReceiver();

        //判断是否显示音乐通知
        if (listManager.getDatum().size() > 0) {
            //显示通知
            NotificationUtil.showMusicNotification(context,
                    listManager.getData(),
                    false,
                    globalLyricManager.isShowing());
        }
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
                if (Constant.ACTION_LIKE.equals(action)) {
                    System.out.println("==========ACTION_LIKE");
                } else if (Constant.ACTION_PREVIOUS.equals(action)) {
                    Song song = listManager.previous();
                    listManager.play(song);
                } else if (Constant.ACTION_PLAY.equals(action)) {

                    if (musicPlayerManager.isPlaying()) {
                        listManager.pause();
                    } else {
                        listManager.resume();
                    }

                } else if (Constant.ACTION_NEXT.equals(action)) {
                    Song song = listManager.next();
                    listManager.play(song);
                } else if (Constant.ACTION_LYRIC.equals(action)) {
                    showOrHideGlobalLyric();
                }

            }

        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_LIKE);
        intentFilter.addAction(Constant.ACTION_PREVIOUS);
        intentFilter.addAction(Constant.ACTION_PLAY);
        intentFilter.addAction(Constant.ACTION_NEXT);
        intentFilter.addAction(Constant.ACTION_LYRIC);
        context.registerReceiver(musicNotificationBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPaused(Song data) {
        NotificationUtil.showMusicNotification(context, data, false, globalLyricManager.isShowing());
    }

    @Override
    public void onPlaying(Song data) {
        NotificationUtil.showMusicNotification(context, data, true, globalLyricManager.isShowing());
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

    }

    @Override
    public void onProgress(Song data) {

    }

    /*
     * 隐藏或显示全局歌词控件
     * */
    private void showOrHideGlobalLyric() {

        if (globalLyricManager.isShowing()) {
            globalLyricManager.hide();
        } else {
            globalLyricManager.show();
        }
        NotificationUtil.showMusicNotification(context, listManager.getData(), musicPlayerManager.isPlaying(), globalLyricManager.isShowing());
    }


}
