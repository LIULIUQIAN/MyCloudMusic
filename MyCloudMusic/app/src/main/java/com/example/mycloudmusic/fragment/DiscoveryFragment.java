package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.DiscoveryAdapter;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Title;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoveryFragment extends BaseCommonFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private DiscoveryAdapter adapter;

    public static DiscoveryFragment newInstance() {

        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, null);
    }

    @Override
    protected void initViews() {
        super.initViews();

        GridLayoutManager layoutManager = new GridLayoutManager(getMainActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new DiscoveryAdapter();
        recyclerView.setAdapter(adapter);

        fetchData();

    }

    /*
     * 请求数据
     * */
    private void fetchData() {

        List<BaseMultiItemEntity> datum = new ArrayList<>();
        //添加标题
        datum.add(new Title("推荐歌单"));

        //添加歌单数据
        for (int i = 0; i < 9; i++) {
            datum.add(new Sheet());
        }

        //添加标题
        datum.add(new Title("推荐单曲"));

        //添加单曲数据
        for (int i = 0; i < 9; i++) {
            datum.add(new Song());
        }

        //将数据设置（替换）到适配器
        adapter.replaceData(datum);

    }
}
