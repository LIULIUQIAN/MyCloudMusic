package com.example.mycloudmusic.manager;

import com.example.mycloudmusic.domain.Song;

import java.util.List;

public interface ListManager {

    /**
     * 设置播放列表
     */
    void setDatum(List<Song> datum);

    /**
     * 获取播放列表
     */
    List<Song> getDatum();

    /**
     * 播放
     */
    void play(Song data);

    /**
     * 暂停
     */
    void pause();

    /**
     * 继续播放
     */
    void resume();

}
