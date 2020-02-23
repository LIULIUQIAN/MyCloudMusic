package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.util.ImageUtil;

public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, User item) {

        ImageUtil.showAvatar(mContext,helper.getView(R.id.iv_banner),item.getAvatar());

        helper.setText(R.id.tv_title,item.getNickname());
        helper.setText(R.id.tv_info,item.getDescriptionFormat());
    }
}
