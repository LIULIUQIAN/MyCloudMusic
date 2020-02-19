package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.VideoDetailActivity;
import com.example.mycloudmusic.adapter.VideoAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import butterknife.BindView;

public class VideoFragment extends BaseCommonFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private VideoAdapter adapter;

    public static VideoFragment newInstance() {

        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, null);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getMainActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new VideoAdapter(R.layout.item_video);
        recyclerView.setAdapter(adapter);

        Api.getInstance().videos().subscribe(new HttpObserver<ListResponse<Video>>() {
            @Override
            public void onSucceeded(ListResponse<Video> data) {

                adapter.replaceData(data.getData());
            }
        });
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                Video item = adapter.getItem(position);
                startActivityExtraId(VideoDetailActivity.class,item.getId());
            }
        });
    }
}
