package com.example.mycloudmusic.domain.lyric;

import java.io.Serializable;
import java.util.List;

/**
 * 解析后的歌词模型
 */
public class Lyric implements Serializable {

    /**
     * 所有的歌词
     */
    private List<Line> datum;

    /**
     * 是否是精确到字的歌词
     */
    private boolean isAccurate;

    public List<Line> getDatum() {
        return datum;
    }

    public void setDatum(List<Line> datum) {
        this.datum = datum;
    }

    public boolean isAccurate() {
        return isAccurate;
    }

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }
}
