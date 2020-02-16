package com.example.mycloudmusic.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.event.DownloadChangedEvent;
import com.example.mycloudmusic.fragment.ConfirmDialogFragment;
import com.example.mycloudmusic.listener.DownloadListener;
import com.example.mycloudmusic.util.FileUtil;
import com.example.mycloudmusic.util.ORMUtil;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.SoftReference;

import butterknife.BindView;
import butterknife.OnClick;

public class DownloadingAdapter extends BaseRecyclerViewAdapter<DownloadInfo, DownloadingAdapter.ViewHolder> {

    private final ORMUtil orm;
    private FragmentManager fragmentManager;
    private DownloadManager downloadManager;

    public DownloadingAdapter(Context context, ORMUtil orm, FragmentManager fragmentManager, DownloadManager downloadManager) {
        super(context);
        this.orm = orm;
        this.fragmentManager = fragmentManager;
        this.downloadManager = downloadManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_downloading, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        DownloadInfo data = getData(position);

        Song song = orm.querySongById(data.getId());
        holder.bindBaseData(song);
        //下载信息
        holder.bindData(data);

    }

    public class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        /**
         * 下载信息
         */
        @BindView(R.id.tv_info)
        TextView tv_info;

        /**
         * 进度条
         */
        @BindView(R.id.pb)
        ProgressBar pb;

        /**
         * 删除按钮
         */
        @BindView(R.id.ib_delete)
        ImageButton ib_delete;

        private DownloadInfo data;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindBaseData(Song song) {
            tv_title.setText(song.getTitle());
        }

        @Override
        public void bindData(Object d) {
            super.bindData(d);

            this.data = (DownloadInfo) d;

            data.setDownloadListener(new DownloadListener(new SoftReference(ViewHolder.this)) {
                @Override
                public void onRefresh() {

                    if (getUserTag() != null && getUserTag().get() != null) {
                        //获取viewHolder
                        ViewHolder holder = (ViewHolder) getUserTag().get();

                        //调用显示数据方法
                        holder.refresh(true);
                    }
                }
            });

            //第一次显示数据
            refresh(false);
        }

        @OnClick(R.id.ib_delete)
        public void onDeleteClcik() {

            ConfirmDialogFragment.show(fragmentManager, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    downloadManager.remove(data);

                    removeData(getAdapterPosition());
                }
            });
        }

        /**
         * 显示下载信息
         */
        private void refresh(boolean isDownloadManagerNotify) {

            switch (data.getStatus()) {
                case DownloadInfo.STATUS_PAUSED:
                    //暂停
                    tv_info.setText(R.string.click_download);
                    pb.setVisibility(View.GONE);
                    break;
                case DownloadInfo.STATUS_ERROR:
                    //下载失败了
                    tv_info.setText(R.string.download_failed);
                    pb.setVisibility(View.GONE);
                    break;
                case DownloadInfo.STATUS_DOWNLOADING:
                case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
                    //下载中
                    //准备下载
                    tv_info.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);

                    //计算进度
                    if (data.getSize() > 0) {
                        //格式化下载进度
                        String start = FileUtil.formatFileSize(data.getProgress());
                        String size = FileUtil.formatFileSize(data.getSize());

                        tv_info.setText(context.getResources().getString(R.string.download_progress, start, size));

                        pb.setMax((int) data.getSize());
                        pb.setProgress((int) data.getProgress());
                    }
                    break;

                //case DownloadInfo.STATUS_COMPLETED:
                //    //下载完成
                //    break;

                case DownloadInfo.STATUS_WAIT:
                    //等待中
                    tv_info.setText(R.string.wait_download);
                    pb.setVisibility(View.GONE);
                    break;
                default:
                    //下载完成
                    //未下载
                    int position = getAdapterPosition();
                    if (position >= 0) {
                        removeData(getAdapterPosition());
                    }

                    //发送通知
                    if (isDownloadManagerNotify) {
                        EventBus.getDefault().post(new DownloadChangedEvent());
                    }
                    break;
            }

        }
    }


}
