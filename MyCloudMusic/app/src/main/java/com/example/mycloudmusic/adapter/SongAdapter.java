package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;


public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_position,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_info,item.getSinger().getNickname());

    }
}
