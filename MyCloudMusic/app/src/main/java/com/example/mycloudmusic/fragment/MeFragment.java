package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.CommentActivity;
import com.example.mycloudmusic.activity.DownloadActivity;
import com.example.mycloudmusic.activity.LocalMusicActivity;
import com.example.mycloudmusic.activity.SheetDetailActivity;
import com.example.mycloudmusic.adapter.MeAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.MeGroup;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.event.CreateSheetClickEvent;
import com.example.mycloudmusic.domain.event.CreateSheetEvent;
import com.example.mycloudmusic.domain.event.SheetChangedEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.ObserverAdapter;
import com.example.mycloudmusic.util.HttpUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

public class MeFragment extends BaseCommonFragment {

    //    @BindView(R.id.elv)
    private ExpandableListView elv;
    private View ll_local_music;
    private View ll_download_manager;

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

        View view = LayoutInflater.from(getMainActivity()).inflate(R.layout.header_me, elv, false);
        ll_local_music = view.findViewById(R.id.ll_local_music);
        ll_download_manager = view.findViewById(R.id.ll_download_manager);
        elv.addHeaderView(view);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        EventBus.getDefault().register(this);

        adapter = new MeAdapter(getMainActivity());
        elv.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {


        Observable<ListResponse<Sheet>> createSheetsApi = Api.getInstance().createSheets(sp.getUserId());
        Observable<ListResponse<Sheet>> collectSheetsApi = Api.getInstance().collectSheets(sp.getUserId());

        Observable.zip(createSheetsApi, collectSheetsApi, new BiFunction<ListResponse<Sheet>, ListResponse<Sheet>, List<MeGroup>>() {
            @Override
            public List<MeGroup> apply(ListResponse<Sheet> createSheets, ListResponse<Sheet> collectSheets) throws Exception {

                List<MeGroup> datum = new ArrayList<>();
                datum.add(new MeGroup("创建的歌单", createSheets.getData(), true));
                datum.add(new MeGroup("收藏的歌单", collectSheets.getData(), false));
                return datum;
            }
        }).subscribe(new ObserverAdapter<List<MeGroup>>() {

            @Override
            public void onNext(List<MeGroup> meGroups) {
                super.onNext(meGroups);
                datum.clear();
                datum.addAll(meGroups);
                adapter.setDatum(datum);

                for (int i = 0; i < datum.size(); i++) {
                    elv.expandGroup(i);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                HttpUtil.handlerRequest(null, e);
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

        ll_local_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LocalMusicActivity.class);
            }
        });
        ll_download_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DownloadActivity.class);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetClickEvent(CreateSheetClickEvent event) {
        CreateSheetDialogFragment.show(getChildFragmentManager());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateSheetEvent(CreateSheetEvent event) {

        Sheet data = new Sheet();
        data.setTitle(event.getData());

        Api.getInstance().createSheet(data).subscribe(new HttpObserver<DetailResponse<Sheet>>() {
            @Override
            public void onSucceeded(DetailResponse<Sheet> data) {

                ToastUtil.successShortToast(R.string.success_create_sheet);
                fetchData();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSheetChangedEvent(SheetChangedEvent event) {
        fetchData();
    }

}
