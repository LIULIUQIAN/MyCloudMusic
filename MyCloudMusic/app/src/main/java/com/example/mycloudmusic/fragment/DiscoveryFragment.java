package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.DiscoveryAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Title;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

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

    /**
     * 创建头部布局
     *
     * @return
     */
    private View createHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_discovery, (ViewGroup) recyclerView.getParent(), false);
        return view;
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new DiscoveryAdapter();
        //设置列宽度
        adapter.setSpanSizeLookup((gridLayoutManager, i) -> adapter.getItem(i).getSpanSize());
        adapter.setHeaderView(createHeaderView());
        recyclerView.setAdapter(adapter);

        fetchData();

    }

    /*
     * 请求数据
     * */
    private void fetchData() {

        List<BaseMultiItemEntity> datum = new ArrayList<>();
        datum.add(new Title("推荐歌单"));

        //添加歌单数据
        Api.getInstance().sheets()
                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        datum.addAll(data.getData());

                        Api.getInstance().songs()
                                .subscribe(new HttpObserver<ListResponse<Song>>() {
                                    @Override
                                    public void onSucceeded(ListResponse<Song> data) {
                                        datum.add(new Title("推荐单曲"));
                                        datum.addAll(data.getData());

                                        adapter.replaceData(datum);
                                    }
                                });
                    }
                });

    }
}
