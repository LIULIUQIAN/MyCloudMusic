package com.example.mycloudmusic.manager;

import com.example.mycloudmusic.domain.Song;

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

}
