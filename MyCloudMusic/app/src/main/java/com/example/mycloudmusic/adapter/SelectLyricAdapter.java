package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.lyric.Line;

public class SelectLyricAdapter extends BaseQuickAdapter<Line, BaseViewHolder> {

    public SelectLyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Line item) {
        helper.setText(R.id.tv_title, item.getData());

        helper.setVisible(R.id.iv_check, item.isChooseState());

        helper.setBackgroundColor(R.id.rl_container,
                item.isChooseState() ?
                        mContext.getResources().getColor(R.color.black) :
                        mContext.getResources().getColor(R.color.transparent));
    }
}
