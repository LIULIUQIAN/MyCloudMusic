package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Book;
import com.example.mycloudmusic.util.ImageUtil;

public class ShopAdapter extends BaseQuickAdapter<Book, BaseViewHolder> {

    public ShopAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Book item) {

        ImageUtil.showAvatar(mContext, helper.getView(R.id.iv_banner), item.getBanner());

        helper.setText(R.id.tv_title, item.getTitle());

        String price = mContext.getResources().getString(R.string.price, item.getPrice());
        helper.setText(R.id.tv_price, price);
    }
}
