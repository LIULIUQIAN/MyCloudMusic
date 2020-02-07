package com.example.mycloudmusic.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.view.LyricLineView;

public class LyricAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {

    private int selectedIndex = 0;

    /**
     * 是否是精确到字歌词
     */
    private boolean accurate;

    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {

        LyricLineView llv = helper.getView(R.id.llv);
        if (item instanceof String) {
            llv.setVisibility(View.GONE);
            llv.setData(null);
            llv.setAccurate(false);
        } else {
            Line data = (Line) item;

            llv.setVisibility(View.VISIBLE);
            llv.setData(data);
            llv.setAccurate(accurate);
        }

        llv.setLineSelected(helper.getAdapterPosition() == selectedIndex);
    }

    public void setSelectedIndex(int selectedIndex) {

        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }

    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }
}
