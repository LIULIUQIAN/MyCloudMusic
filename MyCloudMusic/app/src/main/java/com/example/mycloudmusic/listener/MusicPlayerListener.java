package com.example.mycloudmusic.listener;

import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;

public interface MusicPlayerListener {

    /**
     * 已经暂停了
     */
    default void onPaused(Song data) {
    }

    /**
     * 已经播放了
     */
    default void onPlaying(Song data) {
    }

    /**
     * 播放器准备完毕了
     */
    default void onPrepared(MediaPlayer mp, Song data) {
    }

    /**
     * 播放进度回调
     */
    default void onProgress(Song data) {
    }

    /**
     * 播放完毕
     */
    default void onCompletion(MediaPlayer mp) {
    }

    /**
     * 歌词数据改变了
     *
     * @param data
     */
    default void onLyricChanged(Song data) {

    }

}
