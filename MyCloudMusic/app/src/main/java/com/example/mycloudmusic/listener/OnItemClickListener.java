package com.example.mycloudmusic.listener;

import com.example.mycloudmusic.adapter.BaseRecyclerViewAdapter;

public interface OnItemClickListener {

    /**
     * item点击事件
     */
    void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position);
}


