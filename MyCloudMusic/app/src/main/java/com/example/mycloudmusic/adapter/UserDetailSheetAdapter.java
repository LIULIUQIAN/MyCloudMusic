package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Title;
import com.example.mycloudmusic.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.mycloudmusic.util.Constant.TYPE_SHEET;
import static com.example.mycloudmusic.util.Constant.TYPE_TITLE;

public class UserDetailSheetAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {

    public UserDetailSheetAdapter() {
        super(new ArrayList<>());
        addItemType(TYPE_TITLE, R.layout.item_title_small);
        addItemType(TYPE_SHEET, R.layout.item_topic);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMultiItemEntity item) {

        if (helper.getItemViewType() == TYPE_TITLE){

            Title data = (Title) item;
            helper.setText(R.id.tv_title,data.getTitle());

        }else if (helper.getItemViewType() == TYPE_SHEET){

            Sheet data = (Sheet) item;
            ImageUtil.showAvatar(mContext,helper.getView(R.id.iv_banner),data.getBanner());
            helper.setText(R.id.tv_title,data.getTitle());
            String info = mContext.getResources().getString(R.string.song_count, data.getSongs_count());
            helper.setText(R.id.tv_info, info);

        }

    }
}
