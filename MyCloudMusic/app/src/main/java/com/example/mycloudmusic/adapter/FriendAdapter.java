package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.util.ImageUtil;

public class FriendAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public FriendAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, User item) {
        helper.setText(R.id.tv_title, item.getNickname());
        helper.setText(R.id.tv_info, item.getDescription());

        ImageView iv_banner = helper.getView(R.id.iv_banner);
        ImageUtil.showAvatar((Activity) mContext, iv_banner, item.getAvatar());
    }
}
