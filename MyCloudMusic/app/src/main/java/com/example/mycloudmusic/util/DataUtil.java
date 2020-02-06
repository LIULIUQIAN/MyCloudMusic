package com.example.mycloudmusic.util;

import com.example.mycloudmusic.domain.Song;

import java.util.List;

public class DataUtil {

    /**
     * 更高是否在播放列表字段
     */
    public static void changePlayListFlag(List<Song> datum, boolean value) {
        for (Song data : datum) {
            data.setPlayList(value);
        }
    }
}
