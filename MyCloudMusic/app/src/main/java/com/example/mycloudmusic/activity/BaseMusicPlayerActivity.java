package com.example.mycloudmusic.activity;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.event.PlayListChangedEvent;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.domain.lyric.Lyric;
import com.example.mycloudmusic.fragment.PlayListDialogFragment;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.lyric.LyricUtil;
import com.example.mycloudmusic.view.LyricLineView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class BaseMusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {

    /**
     * 迷你播放控制器 容器
     */
    @BindView(R.id.ll_play_control_small)
    LinearLayout ll_play_control_small;

    /**
     * 迷你播放控制器 封面
     */
    @BindView(R.id.iv_banner_small_control)
    ImageView iv_banner_small_control;

    /**
     * 迷你播放控制器 标题
     */
    @BindView(R.id.tv_title_small_control)
    TextView tv_title_small_control;

    /**
     * 迷你播放控制器 歌词控件
     */
    @BindView(R.id.llv_small_control)
    LyricLineView llv;

    /**
     * 迷你播放控制器 播放暂停按钮
     */
    @BindView(R.id.iv_play_small_control)
    ImageView iv_play_small_control;

    /**
     * 迷你播放控制器 进度条
     */
    @BindView(R.id.pb_progress_small_control)
    ProgressBar pb_progress_small_control;

    protected ListManager listManager;
    protected MusicPlayerManager musicPlayerManager;


    /*
     * 界面显示
     * */
    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        musicPlayerManager.addMusicPlayerListener(this);
        //显示迷你播放控制器数据
        showSmallPlayControlData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeMusicPlayerListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        llv.setLineSelected(true);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        listManager = MusicPlayerService.getListManager(getApplicationContext());
        musicPlayerManager.addMusicPlayerListener(this);
    }

    /**
     * 迷你播放控制器 容器点击
     */
    @OnClick(R.id.ll_play_control_small)
    public void onPlayControlSmallClick() {
        MusicPlayerActivity.start(getMainActivity());
    }

    /**
     * 迷你播放控制器 播放暂停按钮点击
     */
    @OnClick(R.id.iv_play_small_control)
    public void onPlaySmallClick() {

        if (musicPlayerManager.isPlaying()) {
            listManager.pause();
        } else {
            listManager.resume();
        }
    }

    /**
     * 迷你播放控制器 下一曲按钮点击
     */
    @OnClick(R.id.iv_next_small_control)
    public void onNextSmallClick() {

        listManager.play(listManager.next());
    }

    /**
     * 迷你播放控制器 播放列表按钮点击
     */
    @OnClick(R.id.iv_list_small_control)
    public void onListSmallClick() {

        PlayListDialogFragment.show(getSupportFragmentManager());

    }

    /*
     * 显示迷你播放控制器数据
     * */
    private void showSmallPlayControlData() {

        if (listManager.getDatum() != null && listManager.getDatum().size() > 0) {
            ll_play_control_small.setVisibility(View.VISIBLE);
            Song data = listManager.getData();
            if (data == null) {
                return;
            }
            showInitData(data);
            showDuration(data);
            showProgress(data);
            showMusicPlayStatus();
            showLyricData();

        } else {
            ll_play_control_small.setVisibility(View.GONE);
        }

    }

    /*
    * 显示歌词
    * */
    private void showLyricData() {

        Song data = listManager.getData();
        Lyric lyric = data.getParsedLyric();
        if (lyric == null){
            llv.setData(null);
        }else {
            llv.setAccurate(lyric.isAccurate());
            Line line = LyricUtil.getLyricLine(lyric, data.getProgress());
            llv.setData(line);
        }

    }

    /*
     * 显示播放状态
     * */
    private void showMusicPlayStatus() {
        iv_play_small_control.setSelected(musicPlayerManager.isPlaying());
    }

    /*
     * 显示播放进度
     * */
    private void showProgress(Song data) {
        pb_progress_small_control.setProgress((int) data.getProgress());
    }

    /*
     * 显示音乐时长
     * */
    private void showDuration(Song data) {
        pb_progress_small_control.setMax((int) data.getDuration());
    }

    /*
     * 显示初始化数据
     * */
    private void showInitData(Song data) {
        ImageUtil.showAvatar(getMainActivity(), iv_banner_small_control, data.getBanner());
        tv_title_small_control.setText(data.getTitle());

    }

    //音乐播放器回调
    @Override
    public void onPaused(Song data) {
        showMusicPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showMusicPlayStatus();
        showInitData(data);
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        showDuration(data);
    }

    @Override
    public void onProgress(Song data) {
        showProgress(data);

        Lyric lyric = data.getParsedLyric();
        if (lyric == null){
            return;
        }

        long progress = data.getProgress();
        Line line = LyricUtil.getLyricLine(lyric, progress);
        if (line != llv.getData()){
            llv.setData(line);
        }
        if (lyric.isAccurate()){

            int wordIndex = LyricUtil.getWordIndex(line, progress);
            float wordPlayedTime = LyricUtil.getWordPlayedTime(line, progress);

            llv.setLyricCurrentWordIndex(wordIndex);
            llv.setWordPlayedTime(wordPlayedTime);
            llv.onProgress();
        }

    }

    @Override
    public void onLyricChanged(Song data) {
        showLyricData();
    }
    //end音乐播放器回调

    /*
    * 删除音乐事件
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayListChangedEvent(PlayListChangedEvent event){

        showSmallPlayControlData();

    }

}
