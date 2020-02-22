package com.example.mycloudmusic.listener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public interface FeedListener {

    /**
     * 点击了动态图片回调
     */
    void onImageClick(RecyclerView rv, List<String> imageUris, int index);
}
