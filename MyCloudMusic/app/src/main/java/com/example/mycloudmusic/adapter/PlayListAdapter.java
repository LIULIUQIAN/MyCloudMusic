package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;

public class PlayListAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private int selectIndex = -1;

    public PlayListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.addOnClickListener(R.id.ib_delete);

        helper.setText(R.id.tv_title, item.getTitle());
        if (selectIndex == helper.getAdapterPosition()){
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.text));
        }
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }
}
