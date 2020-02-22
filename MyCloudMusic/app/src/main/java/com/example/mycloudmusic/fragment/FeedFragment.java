package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.PublishFeedActivity;
import com.example.mycloudmusic.adapter.FeedAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.event.OnFeedChangedEvent;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.FeedListener;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.google.common.collect.Lists;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedFragment extends BaseCommonFragment implements FeedListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String userId;
    private FeedAdapter adapter;

    public static FeedFragment newInstance(String userId) {

        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getMainActivity()));

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        EventBus.getDefault().register(this);

        userId = extraId();

        adapter = new FeedAdapter(R.layout.item_feed, this);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        Api.getInstance().feeds(userId).subscribe(new HttpObserver<ListResponse<Feed>>() {
            @Override
            public void onSucceeded(ListResponse<Feed> data) {

                adapter.replaceData(data.getData());
            }
        });
    }

    /**
     * 图片点击回调
     */
    @Override
    public void onImageClick(RecyclerView rv, List<String> imageUris, int index) {
        ArrayList<String> list = Lists.newArrayList(imageUris);
        PhotoViewer.INSTANCE
                .setData(list)
                .setCurrentPage(index)
                .setImgContainer(rv)
                .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                    @Override
                    public void show(@NotNull ImageView imageView, @NotNull String s) {
                        Glide.with(getMainActivity()).load(s).into(imageView);
                    }
                })
                .start(this);
    }

    @OnClick(R.id.fab)
    public void onPublishFeedClick() {
        startActivity(PublishFeedActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFeedChangedEvent(OnFeedChangedEvent event) {
        fetchData();
    }
}
