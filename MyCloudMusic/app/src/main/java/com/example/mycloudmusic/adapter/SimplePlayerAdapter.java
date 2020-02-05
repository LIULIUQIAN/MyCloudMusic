package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.domain.Song;

public class SimplePlayerAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {
    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        helper.setText(android.R.id.text1, item.getTitle());
    }
}
