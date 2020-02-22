package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Resource;
import com.example.mycloudmusic.util.ImageUtil;

public class ImageAdapter extends BaseQuickAdapter<Resource, BaseViewHolder> {
    public ImageAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Resource item) {

        ImageUtil.showAvatar(mContext,helper.getView(R.id.iv_banner),item.getUri());

    }
}
