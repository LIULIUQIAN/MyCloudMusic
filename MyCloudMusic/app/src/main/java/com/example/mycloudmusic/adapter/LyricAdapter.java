package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.lyric.Line;

public class LyricAdapter extends BaseQuickAdapter<Line, BaseViewHolder> {
    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Line item) {

        helper.setText(R.id.tv,item.getData());
    }
}
