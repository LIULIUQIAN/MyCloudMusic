package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.MeAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.MeGroup;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MeFragment extends BaseFragment {

//    @BindView(R.id.elv)
    ExpandableListView elv;

    private List<MeGroup> datum = new ArrayList<>();

    private MeAdapter adapter;

    public static MeFragment newInstance() {

        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        elv = findViewById(R.id.elv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new MeAdapter(getMainActivity());
        elv.setAdapter(adapter);

        Api.getInstance().createSheets(sp.getUserId()).subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {

                datum.add(new MeGroup("创建的歌单", data.getData(), true));
                Api.getInstance().collectSheets(sp.getUserId()).subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> model) {
                        datum.add(new MeGroup("收藏的歌单", model.getData(), true));

                        adapter.setDatum(datum);
                    }
                });

            }
        });
    }
}
