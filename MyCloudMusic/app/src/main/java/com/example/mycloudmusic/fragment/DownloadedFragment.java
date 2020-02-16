package com.example.mycloudmusic.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.MusicPlayerActivity;
import com.example.mycloudmusic.adapter.DownloadedAdapter;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.event.DownloadChangedEvent;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadedFragment extends BaseCommonFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private DownloadedAdapter adapter;
    private DownloadManager downloadManager;
    private ListManager listManager;

    public static DownloadedFragment newInstance() {

        DownloadedFragment fragment = new DownloadedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloaded, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

        EventBus.getDefault().register(this);

        downloadManager = AppContext.getInstance().getDownloadManager();
        listManager = MusicPlayerService.getListManager(getMainActivity());

        adapter = new DownloadedAdapter(R.layout.item_downloaded);
        recycler_view.setAdapter(adapter);

        fetchData();

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                listManager.setDatum(adapter.getData());
                listManager.play(adapter.getItem(position));
                startActivity(MusicPlayerActivity.class);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter a, View view, int position) {

                ConfirmDialogFragment.show(getFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DownloadInfo downloadInfo = downloadManager.getDownloadById(adapter.getItem(position).getId());
                        downloadManager.remove(downloadInfo);
                        adapter.remove(position);
                    }
                });
            }
        });

    }

    private void fetchData() {

        List<DownloadInfo> downloads = downloadManager.findAllDownloaded();

        List<Song> datum = new ArrayList<>();

        for (DownloadInfo download : downloads) {

            Song song = orm.querySongById(download.getId());
            datum.add(song);
        }
        adapter.replaceData(datum);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDownloadChangedEvent(DownloadChangedEvent event) {
        fetchData();
    }


}
