package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.SearchHistory;

public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistory, BaseViewHolder> {

    public SearchHistoryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchHistory item) {
        helper.setText(R.id.tv_title, item.getContent());

        helper.addOnClickListener(R.id.iv_delete);
    }
}
