package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;

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

    }
}
