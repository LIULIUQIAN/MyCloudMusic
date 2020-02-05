package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;


public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private int selectIndex = -1;

    public SongAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_position,String.valueOf(helper.getAdapterPosition()));
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_info,item.getSinger().getNickname());

        if (selectIndex == helper.getAdapterPosition()){
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.text));
        }

    }

    public void setSelectIndex(int selectIndex) {

        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }

        this.selectIndex = selectIndex;
        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }
    }
}
