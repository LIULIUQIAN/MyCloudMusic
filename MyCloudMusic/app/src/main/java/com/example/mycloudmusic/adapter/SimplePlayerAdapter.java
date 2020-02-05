package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;

import java.util.ArrayList;
import java.util.List;

public class SimplePlayerAdapter extends BaseItemDraggableAdapter<Song, BaseViewHolder> {

    private int selectIndex = -1;

    public SimplePlayerAdapter(int layoutResId) {
        super(layoutResId, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {
        helper.setText(android.R.id.text1, item.getTitle());

        if (selectIndex == helper.getAdapterPosition()){
            helper.setTextColor(android.R.id.text1,mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            helper.setTextColor(android.R.id.text1,mContext.getResources().getColor(R.color.text));
        }
    }

    public void setSelectIndex(Integer selectIndex) {

        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }

        this.selectIndex = selectIndex;
        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }
    }
}
