package com.example.mycloudmusic.manager.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.GlobalLyricListener;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.GlobalLyricManager;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.NotificationUtil;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.example.mycloudmusic.util.SizeUtil;
import com.example.mycloudmusic.view.GlobalLyricView;

import static com.example.mycloudmusic.util.Constant.ACTION_UNLOCK_LYRIC;

public class GlobalLyricManagerImpl implements GlobalLyricManager, GlobalLyricListener, MusicPlayerListener {

    private static GlobalLyricManagerImpl instance;
    private final Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private GlobalLyricView globalLyricView;
    private final PreferenceUtil sp;
    private final ListManager listManager;
    private final MusicPlayerManager musicPlayerManager;
    private BroadcastReceiver unlockGlobalLyricBroadcastReceiver;

    private GlobalLyricManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        listManager = MusicPlayerService.getListManager(this.context);
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);
        sp = PreferenceUtil.getInstance(this.context);

        initWindowManager();

        if (sp.isShowGlobalLyric()) {
            initGlobalLyricView();

            if (sp.isGlobalLyricLock()) {
                lock();
            }
        }
    }


    public static GlobalLyricManagerImpl getInstance(Context context) {

        if (instance == null) {
            instance = new GlobalLyricManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void show() {

        initGlobalLyricView();

        sp.setShowGlobalLyric(true);
    }

    @Override
    public void hide() {

        windowManager.removeView(globalLyricView);
        globalLyricView = null;

        sp.setShowGlobalLyric(false);

        NotificationUtil.showMusicNotification(context, listManager.getData(), musicPlayerManager.isPlaying(), false);

    }

    @Override
    public boolean isShowing() {
        return globalLyricView != null;
    }

    @Override
    public void tryHide() {

        if (sp.isShowGlobalLyric()) {
            globalLyricView.setVisibility(View.GONE);
        }
    }

    @Override
    public void tryShow() {
        if (sp.isShowGlobalLyric()) {
            globalLyricView.setVisibility(View.VISIBLE);
        }
    }

    /*
     * 初始化窗口管理器
     * */
    private void initWindowManager() {

        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            layoutParams = new WindowManager.LayoutParams();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            layoutParams.format = PixelFormat.RGB_888;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            layoutParams.width = dm.widthPixels;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.y = sp.getGlobalLyricViewY();

            //设置全局歌词view状态
            setGlobalLyricStatus();
        }


    }

    /*
     * 创建全局歌词View
     * */
    private void initGlobalLyricView() {


        if (globalLyricView == null) {
            globalLyricView = new GlobalLyricView(context);
            globalLyricView.setGlobalLyricListener(this);
        }

        if (globalLyricView.getParent() == null) {
            windowManager.addView(globalLyricView, layoutParams);
        }

        showLyricData();

        //显示播放状态
        globalLyricView.setPlay(musicPlayerManager.isPlaying());

        //显示音乐通知
        NotificationUtil.showMusicNotification(context,
                listManager.getData(),
                musicPlayerManager.isPlaying(),
                true);

    }

    //全局歌词监听器////////////////////////////////////////////////////////////////////////////
    @Override
    public void onLogoClick() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Constant.ACTION_MUSIC_PLAY_CLICK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onCloseClick() {
        hide();
    }

    @Override
    public void onLockClick() {
        lock();
    }


    @Override
    public void onPreviousClick() {
        listManager.play(listManager.previous());
    }

    @Override
    public void onPlayClick() {

        if (musicPlayerManager.isPlaying()) {
            listManager.pause();
        } else {
            listManager.resume();
        }
    }

    @Override
    public void onNextClick() {
        listManager.play(listManager.next());
    }

    @Override
    public void onGlobalLyricDrag(int y) {

        layoutParams.y = y - SizeUtil.getStatusBarHeight(context);
        updateView();
        sp.setGlobalLyricViewY(layoutParams.y);
    }


    //end全局歌词监听器////////////////////////////////////////////////////////////////////////////


    //播放器监听器////////////////////////////////////////////////////////////////////////////

    @Override
    public void onPaused(Song data) {
        if (globalLyricView != null) {
            globalLyricView.setPlay(false);
        }
    }

    @Override
    public void onPlaying(Song data) {
        if (globalLyricView != null) {
            globalLyricView.setPlay(true);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

    }

    @Override
    public void onProgress(Song data) {

        if (globalLyricView != null) {
            globalLyricView.onProgress(data);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onLyricChanged(Song data) {

        showLyricData();
    }

    //end播放器监听器////////////////////////////////////////////////////////////////////////////

    /**
     * 显示歌词数据
     */
    private void showLyricData() {

        if (globalLyricView == null) {
            return;
        }

        if (listManager.getData() == null || listManager.getData().getParsedLyric() == null) {
            globalLyricView.clearLyric();
        } else {
            globalLyricView.showSecondLyricView();
            globalLyricView.setAccurate(listManager.getData().getParsedLyric().isAccurate());

            onProgress(listManager.getData());
        }

    }

    /*
     * 歌词锁定
     * */
    private void lock() {

        sp.setGlobalLyricLock(true);

        globalLyricView.simpleStyle();

        setGlobalLyricStatus();

        updateView();

        NotificationUtil.showUnlockGlobalLyricNotification(context);

        registerUnlockGlobalLyricReceiver();

    }

    /*
     * 歌词解锁
     * */
    private void unlock() {

        sp.setGlobalLyricLock(false);
        globalLyricView.normalStyle();
        setGlobalLyricStatus();
        updateView();
        unregisterUnlockGlobalLyricReceiver();
        //清除歌词解锁通知
        //不用清除
        NotificationUtil.clearUnlockGlobalLyricNotification(context);
    }

    /**
     * 注册接收解锁全局歌词广告接收器
     */
    private void registerUnlockGlobalLyricReceiver() {

        if (unlockGlobalLyricBroadcastReceiver == null) {
            unlockGlobalLyricBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    if (intent.getAction().equals(ACTION_UNLOCK_LYRIC)) {
                        unlock();
                    }
                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_UNLOCK_LYRIC);
            context.registerReceiver(unlockGlobalLyricBroadcastReceiver, intentFilter);
        }

    }

    /**
     * 解除接收全局歌词事件广播接受者
     */
    private void unregisterUnlockGlobalLyricReceiver() {
        if (unlockGlobalLyricBroadcastReceiver != null) {
            context.unregisterReceiver(unlockGlobalLyricBroadcastReceiver);
            unlockGlobalLyricBroadcastReceiver = null;
        }
    }

    /**
     * 更新布局
     */
    private void updateView() {
        windowManager.updateViewLayout(globalLyricView, layoutParams);
    }

    /**
     * 设置全局歌词控件状态
     */
    private void setGlobalLyricStatus() {

        if (sp.isGlobalLyricLock()) {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }


    }
}
