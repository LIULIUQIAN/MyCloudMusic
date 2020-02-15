package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.CommentActivity;
import com.example.mycloudmusic.activity.SheetDetailActivity;
import com.example.mycloudmusic.adapter.MeAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.MeGroup;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.event.CreateSheetClickEvent;
import com.example.mycloudmusic.domain.event.CreateSheetEvent;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MeFragment extends BaseCommonFragment {

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        elv = findViewById(R.id.elv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        EventBus.getDefault().register(this);

        adapter = new MeAdapter(getMainActivity());
        elv.setAdapter(adapter);

        Api.getInstance().createSheets(sp.getUserId()).subscribe(new HttpObserver<ListResponse<Sheet>>() {
            @Override
            public void onSucceeded(ListResponse<Sheet> data) {

                datum.add(new MeGroup("创建的歌单", data.getData(), true));
                Api.getInstance().collectSheets(sp.getUserId()).subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> model) {
                        datum.add(new MeGroup("收藏的歌单", model.getData(), false));

                        adapter.setDatum(datum);

                        for (int i=0;i < datum.size();i++){
                            elv.expandGroup(i);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Sheet sheet = datum.get(groupPosition).getDatum().get(childPosition);

                startActivityExtraId(SheetDetailActivity.class, sheet.getId());

                return true;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetClickEvent(CreateSheetClickEvent event){
        CreateSheetDialogFragment.show(getChildFragmentManager());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetEvent(CreateSheetEvent event) {
        Log.e("onCreateSheetEvent","onCreateSheetEvent"+event.getData());
    }
}
