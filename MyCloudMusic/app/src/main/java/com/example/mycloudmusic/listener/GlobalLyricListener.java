package com.example.mycloudmusic.listener;

public interface GlobalLyricListener {

    /**
     * logo点击
     */
    void onLogoClick();

    /**
     * 关闭点击
     */
    void onCloseClick();

    /**
     * 锁定点击
     */
    void onLockClick();

    /**
     * 上一首点击
     */
    void onPreviousClick();

    /**
     * 播放点击
     */
    void onPlayClick();

    /**
     * 下一首点击
     */
    void onNextClick();

    /**
     * 拖拽的方法
     */
    void onGlobalLyricDrag(int y);
}
