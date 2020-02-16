package com.example.mycloudmusic.listener;

import com.ixuea.android.downloader.callback.AbsDownloadListener;
import com.ixuea.android.downloader.exception.DownloadException;

import java.lang.ref.SoftReference;

public abstract class DownloadListener extends AbsDownloadListener {

    public DownloadListener() {
    }

    public DownloadListener(SoftReference<Object> userTag) {
        super(userTag);
    }

    @Override
    public void onStart() {

        onRefresh();

    }

    @Override
    public void onWaited() {
        onRefresh();
    }

    @Override
    public void onPaused() {
        onRefresh();
    }

    @Override
    public void onDownloading(long progress, long size) {
        onRefresh();
    }

    @Override
    public void onRemoved() {
        onRefresh();
    }

    @Override
    public void onDownloadSuccess() {
        onRefresh();
    }

    @Override
    public void onDownloadFailed(DownloadException e) {
        onRefresh();
    }

    /**
     * 刷新状态
     */
    public abstract void onRefresh();
}
