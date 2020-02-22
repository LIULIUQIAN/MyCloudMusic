package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.FeedAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;

public class FeedFragment extends BaseCommonFragment {

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

        userId = extraId();

        adapter = new FeedAdapter(R.layout.item_feed);
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
}
