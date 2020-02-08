package com.example.mycloudmusic.domain.lyric;


import com.example.mycloudmusic.domain.BaseModel;

/**
 * 一行歌词
 */
public class Line extends BaseModel {
    /**
     * 整行歌词
     */
    private String data;

    /**
     * 开始时间
     * 单位毫秒
     */
    private long startTime;

    /**
     * 每一个字
     */
    private String[] words;

    /**
     * 每一个字对应的时间
     */
    private Integer[] wordDurations;

    /**
     * 结束时间
     */
    private long endTime;

    /*
    * 是否选中
    * */
    private boolean chooseState;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public Integer[] getWordDurations() {
        return wordDurations;
    }

    public void setWordDurations(Integer[] wordDurations) {
        this.wordDurations = wordDurations;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isChooseState() {
        return chooseState;
    }

    public void setChooseState(boolean chooseState) {
        this.chooseState = chooseState;
    }
}
