package com.example.mycloudmusic.manager;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.impl.MusicNotificationManager;

public interface MusicPlayerManager {

    /**
     * 播放
     */
    void play(String uri, Song data);

    /**
     * 是否正在播放
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

    /**
     * 添加播放监听器
     */
    void addMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 移除播放监听器
     */
    void removeMusicPlayerListener(MusicPlayerListener listener);

    /**
     * 获取当前播放的音乐
     */
    Song getData();

    /**
     * 从指定位置播放(单位:毫秒)
     */
    void seekTo(int progress);
    /**
     * 设置是否单曲循环
     */
    void setLooping(boolean looping);
}
