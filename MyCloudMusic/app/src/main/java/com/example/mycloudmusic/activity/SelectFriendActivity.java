package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.FriendAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.event.SelectedFriendEvent;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class SelectFriendActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getMainActivity()));

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(decoration);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new FriendAdapter(R.layout.item_user);
        recycler_view.setAdapter(adapter);

        fetchData();
    }

    /**
     * 请求网络数据
     */
    private void fetchData() {
        Api.getInstance().friends(sp.getUserId()).subscribe(new HttpObserver<ListResponse<User>>() {
            @Override
            public void onSucceeded(ListResponse<User> data) {
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

                EventBus.getDefault().post(new SelectedFriendEvent(adapter.getItem(position)));
                finish();
            }
        });
    }
}
