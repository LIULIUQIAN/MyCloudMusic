package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.BaseRecyclerViewAdapter;
import com.example.mycloudmusic.adapter.DownloadingAdapter;
import com.example.mycloudmusic.listener.OnItemClickListener;
import com.example.mycloudmusic.util.ToastUtil;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadingFragment extends BaseCommonFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.bt_download)
    Button bt_download;

    private DownloadingAdapter adapter;
    private DownloadManager downloadManager;

    public static DownloadingFragment newInstance() {

        DownloadingFragment fragment = new DownloadingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloading, container, false);
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

        downloadManager = AppContext.getInstance().getDownloadManager();

        adapter = new DownloadingAdapter(getMainActivity(), orm, getFragmentManager(), downloadManager);
        recycler_view.setAdapter(adapter);

        List<DownloadInfo> downloading = downloadManager.findAllDownloading();
        adapter.setDatum(downloading);

        //显示按钮状态
        showButtonStatus();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                DownloadInfo data = adapter.getData(position);
                switch (data.getStatus()) {
                    case DownloadInfo.STATUS_NONE:
                    case DownloadInfo.STATUS_PAUSED:
                    case DownloadInfo.STATUS_ERROR:
                        //继续下载
                        downloadManager.resume(data);
                        break;
                    default:
                        //暂停下载
                        downloadManager.pause(data);
                        break;
                }

                //显示按钮状态
                showButtonStatus();
            }
        });
    }

    /**
     * 显示按钮状态
     */
    private void showButtonStatus() {
        if (isDownloading()) {
            bt_download.setText(R.string.pause_all);
        } else {
            bt_download.setText(R.string.download_all);
        }
    }

    /**
     * 是否有下载任务
     */
    private boolean isDownloading() {
        //获取所有下载任务
        List<DownloadInfo> datum = adapter.getDatum();

        //遍历所有下载任务
        for (DownloadInfo downloadInfo : datum) {
            if (downloadInfo.getStatus() == DownloadInfo.STATUS_DOWNLOADING) {
                //只要有一个是下载
                return true;
            }
        }

        return false;
    }

    @OnClick(R.id.bt_download)
    public void onDownloadClick() {
        if (adapter.getItemCount() == 0) {
            ToastUtil.errorShortToast(R.string.error_not_download);
            return;
        }
        if (isDownloading()) {
            downloadManager.pauseAll();
        } else {
            downloadManager.resumeAll();
        }
        adapter.notifyDataSetChanged();

        //显示按钮状态
        showButtonStatus();
    }

    @OnClick(R.id.bt_delete)
    public void onDeleteClick() {

        if (adapter.getItemCount() == 0) {
            ToastUtil.errorShortToast(R.string.error_not_download);
            return;
        }
        //删除所有下载任务
        for (DownloadInfo downloadInfo : adapter.getDatum()) {
            downloadManager.remove(downloadInfo);
        }

        //清除适配器数据
        adapter.clearData();

    }
}
