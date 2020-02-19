package com.example.mycloudmusic.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.TimeUtil;

public class VideoAdapter extends BaseQuickAdapter<Video, BaseViewHolder> {
    public VideoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Video item) {

        ImageUtil.showAvatar((Activity) mContext,helper.getView(R.id.iv_banner),item.getBanner(),10);

        helper.setText(R.id.tv_count,String.valueOf(item.getClicks_count()));

        helper.setText(R.id.tv_time,
                TimeUtil.s2ms((int) item.getDuration()));

        helper.setText(R.id.tv_title,item.getTitle());

        ImageUtil.showAvatar(mContext,helper.getView(R.id.iv_avatar),item.getUser().getAvatar());

        helper.setText(R.id.tv_nickname,item.getUser().getNickname());

        helper.setText(R.id.tv_likes_count,String.valueOf(0));

        helper.setText(R.id.tv_comments_count,String.valueOf(item.getComments_count()));
    }
}
