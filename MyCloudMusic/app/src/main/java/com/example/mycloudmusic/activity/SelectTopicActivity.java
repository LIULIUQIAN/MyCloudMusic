package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.TopicAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Topic;
import com.example.mycloudmusic.domain.event.SelectedTopicEvent;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class SelectTopicActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_topic);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getMainActivity()));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(itemDecoration);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new TopicAdapter(R.layout.item_topic);
        recycler_view.setAdapter(adapter);

        fetchData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                EventBus.getDefault().post(new SelectedTopicEvent(adapter.getItem(position)));

                finish();
            }
        });
    }

    /*
     * 请求网络数据
     * */
    private void fetchData() {

        Api.getInstance().topics().subscribe(new HttpObserver<ListResponse<Topic>>() {
            @Override
            public void onSucceeded(ListResponse<Topic> data) {

                adapter.replaceData(data.getData());
            }
        });

    }
}
