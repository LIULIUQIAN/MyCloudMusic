package com.example.mycloudmusic.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.ImageUtil;
import com.luck.picture.lib.entity.LocalMedia;

public class ImageSelectAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
    public ImageSelectAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {

        ImageView imageView = helper.getView(R.id.iv_banner);
        if (item instanceof Integer){
            imageView.setImageResource((Integer) item);
            helper.setVisible(R.id.iv_delete,false);
        }else {
            LocalMedia localMedia = (LocalMedia) item;
            ImageUtil.show(mContext,imageView,localMedia.getCompressPath());
            helper.setVisible(R.id.iv_delete,true);
        }

        helper.addOnClickListener(R.id.iv_delete);

    }
}
