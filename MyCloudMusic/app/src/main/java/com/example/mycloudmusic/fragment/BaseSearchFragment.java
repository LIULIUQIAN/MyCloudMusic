package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.event.OnSearchEvent;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class BaseSearchFragment extends BaseCommonFragment {

    @BindView(R.id.rv)
    RecyclerView rv;
    private int index;


    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //初始化列表控件
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        index = getArguments().getInt(Constant.ID, -1);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchEvent(OnSearchEvent event){


        if (event.getSelectedIndex() == index){
            fetchData(event.getData());
        }
    }

    /**
     * 执行网络查询
     */
    protected void fetchData(String data) {

    }
}
