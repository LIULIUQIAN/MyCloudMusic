package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.UserDetailSheetAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Title;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserDetailSheetFragment extends BaseCommonFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private String userId;
    private UserDetailSheetAdapter adapter;

    public static UserDetailSheetFragment getInstance(String userId) {
        UserDetailSheetFragment fragment = new UserDetailSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_sheet, container, false);
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

        adapter = new UserDetailSheetAdapter();
        recycler_view.setAdapter(adapter);

        userId = extraId();
        fetchData();

    }

    private void fetchData() {
        List<BaseMultiItemEntity> datum = new ArrayList<>();

        datum.add(new Title("创建的歌单"));

        Api.getInstance().createSheets(userId).subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {
                datum.addAll(data.getData());

                Api.getInstance().collectSheets(userId).subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> d) {
                        datum.add(new Title("收藏的歌单"));
                        datum.addAll(d.getData());

                        adapter.replaceData(datum);
                    }
                });
            }
        });

    }
}
