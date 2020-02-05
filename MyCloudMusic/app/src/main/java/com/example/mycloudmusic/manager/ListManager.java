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
     * 获取当前播放的音乐
     *
     * @return
     */
    Song getData();

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

    /**
     * 获取上一个
     */
    Song previous();

    /**
     * 获取下一个
     */
    Song next();

    /**
     * 更改循环模式
     */
    int changeLoopModel();

    /**
     * 获取循环模式
     */
    int getLoopModel();

    /**
     * 删除音乐
     */
    void delete(int index);


}
