package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.media.Image;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Topic;
import com.example.mycloudmusic.util.ImageUtil;

public class TopicAdapter extends BaseQuickAdapter<Topic, BaseViewHolder> {

    public TopicAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Topic item) {

        helper.setText(R.id.tv_title,String.format("#%s#",item.getTitle()));
        helper.setText(R.id.tv_info,item.getDescription());

        helper.setText(R.id.tv_info, mContext.getResources().getString(R.string.join_count, item.getJoins_count()));

        ImageView iv_banner = helper.getView(R.id.iv_banner);
        ImageUtil.showAvatar((Activity) mContext,iv_banner,item.getBanner());

    }
}
