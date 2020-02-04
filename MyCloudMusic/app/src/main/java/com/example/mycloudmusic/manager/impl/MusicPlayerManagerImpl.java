package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.Consumer;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.mycloudmusic.util.Constant.MESSAGE_PROGRESS;

public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl instance;
    private final Context context;
    private final MediaPlayer player;
    private Song data;

    List<MusicPlayerListener> listeners = new ArrayList<>();
    private TimerTask timerTask;
    private Timer timer;

    private MusicPlayerManagerImpl(Context context) {
        this.context = context;

        player = new MediaPlayer();
        initListeners();
    }

    public static synchronized MusicPlayerManagerImpl getInstance(Context context) {

        if (instance == null) {
            instance = new MusicPlayerManagerImpl(context);
        }
        return instance;
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

            startPublishProgress();

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
        stopPublishProgress();
    }

    @Override
    public void resume() {

        if (!isPlaying()) {
            player.start();

            ListUtil.eachListener(listeners, listener -> listener.onPlaying(data));

            startPublishProgress();
        }
    }

    @Override
    public void addMusicPlayerListener(MusicPlayerListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        startPublishProgress();
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

    @Override
    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    @Override
    public void setLooping(boolean looping) {
        player.setLooping(looping);
    }

    /**
     * 启动播放进度通知
     */
    private void startPublishProgress() {

        if (isEmptyListeners()){
            return;
        }
        if (!isPlaying()){
            return;
        }
        if (timerTask != null){
            return;
        }

        timerTask = new TimerTask(){
            @Override
            public void run() {
                if (isEmptyListeners()){
                    stopPublishProgress();
                    return;
                }
                handler.sendEmptyMessage(MESSAGE_PROGRESS);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,16);
    }

    /**
     * 停止播放进度通知
     */
    private void stopPublishProgress() {

        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
    /**
     * 是否没有进度监听器
     *
     * @return
     */
    private boolean isEmptyListeners() {
        return listeners.size() == 0;
    }

    Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_PROGRESS:
                    data.setProgress(player.getCurrentPosition());
                    ListUtil.eachListener(listeners, listener -> listener.onProgress(data));
                    break;
            }
        }
    };
}
