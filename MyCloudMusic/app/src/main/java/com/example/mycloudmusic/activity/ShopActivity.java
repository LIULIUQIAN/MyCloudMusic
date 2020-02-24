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
import com.example.mycloudmusic.adapter.ShopAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Book;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import butterknife.BindView;

public class ShopActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
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

        adapter = new ShopAdapter(R.layout.item_shop);
        recyclerView.setAdapter(adapter);

        Api.getInstance().shops().subscribe(new HttpObserver<ListResponse<Book>>() {
            @Override
            public void onSucceeded(ListResponse<Book> data) {
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

                startActivityExtraId(ShopDetailActivity.class,adapter.getItem(position).getId());
            }
        });

    }
}
