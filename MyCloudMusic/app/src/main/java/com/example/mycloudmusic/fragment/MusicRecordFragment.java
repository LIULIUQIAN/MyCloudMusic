package com.example.mycloudmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.event.OnStartRecordEvent;
import com.example.mycloudmusic.domain.event.OnStopRecordEvent;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MusicRecordFragment extends BaseCommonFragment {

    /**
     * 黑胶唱片容器
     */
    @BindView(R.id.cl_content)
    ConstraintLayout cl_content;

    /**
     * 歌曲封面
     */
    @BindView(R.id.iv_banner)
    CircleImageView iv_banner;
    private Song song;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_music, null);
    }

    public static MusicRecordFragment getInstance(Song song) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, song);
        MusicRecordFragment fragment = new MusicRecordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        song = (Song) getArguments().getSerializable(Constant.DATA);
        ImageUtil.showAvatar(getMainActivity(),iv_banner, song.getBanner());

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartRecordEvent(OnStartRecordEvent event){

        if (song == event.getData()){
            startRecordRotate();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopRecordEvent(OnStopRecordEvent event){
        if (song == event.getData()){
            stopRecordRotate();
        }
    }

    /**
     * 停止动画
     */
    private void stopRecordRotate() {

        Log.e("","停止动画");

    }
    /**
     * 开始动画
     */
    private void startRecordRotate() {

        Log.e("","开始动画");
    }
}
