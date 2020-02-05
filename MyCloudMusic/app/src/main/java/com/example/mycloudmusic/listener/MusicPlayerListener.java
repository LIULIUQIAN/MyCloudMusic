package com.example.mycloudmusic.listener;

import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;

public interface MusicPlayerListener {

    /**
     * 已经暂停了
     */
    void onPaused(Song data);

    /**
     * 已经播放了
     */
    void onPlaying(Song data);

    /**
     * 播放器准备完毕了
     */
    void onPrepared(MediaPlayer mp, Song data);

    /**
     * 播放进度回调
     */
    void onProgress(Song data);

    /**
     * 播放完毕
     */
    void onCompletion(MediaPlayer mp);
}
