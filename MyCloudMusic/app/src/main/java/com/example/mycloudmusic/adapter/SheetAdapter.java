package com.example.mycloudmusic.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.util.ImageUtil;

public class SheetAdapter extends BaseQuickAdapter<Sheet, BaseViewHolder> {

    public SheetAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Sheet item) {

        ImageUtil.showAvatar(mContext,helper.getView(R.id.iv_banner),item.getBanner());

        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_info,mContext.getResources().getString(R.string.song_count,item.getSongsCount()));
    }
}
