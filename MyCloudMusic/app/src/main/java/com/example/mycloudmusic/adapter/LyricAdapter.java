package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.lyric.Line;

public class LyricAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private int selectedIndex = 0;

    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {

        if (item instanceof String) {
            helper.setText(R.id.tv, "");
        } else {
            Line data = (Line) item;
            helper.setText(R.id.tv, data.getData());
        }

        if (helper.getAdapterPosition() == selectedIndex) {
            helper.setTextColor(R.id.tv, mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            helper.setTextColor(R.id.tv, mContext.getResources().getColor(R.color.lyric_text_color));
        }
    }

    public void setSelectedIndex(int selectedIndex) {

        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }
}
