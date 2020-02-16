package com.example.mycloudmusic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;


public class SongAdapter extends BaseQuickAdapter<Song, BaseViewHolder> {

    private int selectIndex = -1;
    private final DownloadManager downloadManager;

    public SongAdapter(int layoutResId) {
        super(layoutResId);

        downloadManager = AppContext.getInstance().getDownloadManager();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Song item) {

        helper.setText(R.id.tv_position,String.valueOf(helper.getAdapterPosition()));
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setText(R.id.tv_info,item.getSinger().getNickname());

        if (selectIndex == helper.getAdapterPosition()){
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.text));
        }

        helper.addOnClickListener(R.id.ib_more);

        DownloadInfo downloadInfo = downloadManager.getDownloadById(item.getId());

        if (downloadInfo!= null && downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED){
            helper.setVisible(R.id.iv_download,true);
        }else {
            helper.setVisible(R.id.iv_download,false);
        }

    }

    public void setSelectIndex(int selectIndex) {

        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }

        this.selectIndex = selectIndex;
        if (this.selectIndex != -1){
            notifyItemChanged(this.selectIndex);
        }
    }
}
