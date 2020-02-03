package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Title;
import com.example.mycloudmusic.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.mycloudmusic.util.Constant.TYPE_SHEET;
import static com.example.mycloudmusic.util.Constant.TYPE_SONG;
import static com.example.mycloudmusic.util.Constant.TYPE_TITLE;

public class DiscoveryAdapter extends BaseMultiItemQuickAdapter<BaseMultiItemEntity, BaseViewHolder> {

    public DiscoveryAdapter() {
        super(new ArrayList<>());

        addItemType(TYPE_TITLE, R.layout.item_title);
        addItemType(TYPE_SHEET, R.layout.item_sheet);
        addItemType(TYPE_SONG, R.layout.item_song);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BaseMultiItemEntity item) {

        switch (item.getItemType()) {
            case TYPE_TITLE:
                Title title = (Title) item;
                helper.setText(R.id.tv_title, title.getTitle());
                break;

            case TYPE_SHEET:
                Sheet sheet = (Sheet) item;
                helper.setText(R.id.tv_info, String.valueOf(sheet.getClicks_count()));
                helper.setText(R.id.tv_title, sheet.getTitle());
                ImageUtil.showAvatar((Activity) mContext, helper.getView(R.id.iv_banner), sheet.getBanner());
                break;

            case TYPE_SONG:
                Song song = (Song) item;
                ImageUtil.showAvatar((Activity) mContext, helper.getView(R.id.iv_banner), song.getBanner());
                helper.setText(R.id.tv_title, song.getTitle());
                helper.setText(R.id.tv_info, String.valueOf(song.getClicks_count()));
                ImageUtil.showCircleAvatar((Activity) mContext, helper.getView(R.id.iv_avatar), song.getSinger().getAvatar());
                helper.setText(R.id.tv_nickname, song.getSinger().getNickname());
                break;
        }
    }
}
