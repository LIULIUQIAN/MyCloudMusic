package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.SheetDetailActivity;
import com.example.mycloudmusic.adapter.SheetAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

public class SheetFragment extends BaseSearchFragment {

    private SheetAdapter adapter;

    public static SheetFragment newInstance(int position){

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID,position);

        SheetFragment fragment = new SheetFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new SheetAdapter(R.layout.item_topic);
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                startActivityExtraId(SheetDetailActivity.class,adapter.getItem(position).getId());
            }
        });
    }

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);

        Api.getInstance().searchSheets(data).subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {

                adapter.replaceData(data.getData());
            }
        });

    }
}
