package com.example.mycloudmusic.listener;

import com.example.mycloudmusic.domain.MatchResult;

public interface OnTagClickListener {

    /**
     * 点击回调方法
     *
     * @param data        点击的内容
     * @param matchResult 点击范围
     */
    void onTagClick(String data, MatchResult matchResult);
}
