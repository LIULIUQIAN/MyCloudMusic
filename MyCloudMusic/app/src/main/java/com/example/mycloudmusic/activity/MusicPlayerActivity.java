package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.MusicPlayerAdapter;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.domain.event.PlayListChangedEvent;
import com.example.mycloudmusic.fragment.PlayListDialogFragment;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.SwitchDrawableUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.Instant;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_LIST;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_ONE;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_RANDOM;

public class MusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {

    @BindView(R.id.iv_background)
    ImageView iv_background;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tv_start)
    TextView tv_start;

    @BindView(R.id.sb_progress)
    SeekBar sb_progress;

    @BindView(R.id.tv_end)
    TextView tv_end;

    @BindView(R.id.ib_play)
    ImageButton ib_play;

    @BindView(R.id.ib_loop_model)
    ImageButton ib_loop_model;


    private ListManager listManager;
    private MusicPlayerManager musicPlayerManager;
    private MusicPlayerAdapter recordAdapter;

    public static void start(Activity activity) {

        Intent intent = new Intent(activity, MusicPlayerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayerManager.addMusicPlayerListener(this);
        EventBus.getDefault().register(this);

        //显示初始化数据
        showInitData();

        //显示音乐时长
        showDuration();

        //显示播放进度
        showProgress();

        //显示播放状态
        showMusicPlayStatus();

        //显示循环模式
        showLoopModel();


    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeMusicPlayerListener(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ToastUtil.successShortToast("点击了分享");
            return true;
        } else if (item.getItemId() == R.id.action_sort) {
            ToastUtil.successShortToast("点击了排序");
            return true;
        } else if (item.getItemId() == R.id.action_report) {
            ToastUtil.successShortToast("点击了举报");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViews() {
        super.initViews();

        lightStatusBar();

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        listManager = MusicPlayerService.getListManager(getApplicationContext());
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

        recordAdapter = new MusicPlayerAdapter(getMainActivity(), getSupportFragmentManager());
        viewPager.setAdapter(recordAdapter);
        recordAdapter.setDatum(listManager.getDatum());
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        //进度拖拽
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    listManager.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*
     * 显示初始化数据
     * */
    @SuppressLint("CheckResult")
    private void showInitData() {

        Song song = listManager.getData();
        setTitle(song.getTitle());
        toolbar.setSubtitle(song.getSinger().getNickname());
//        ImageUtil.showAvatar(getMainActivity(),iv_background,song.getBanner());
        RequestBuilder<Drawable> requestBuilder = Glide.with(this).asDrawable();
        if (StringUtils.isBlank(song.getBanner())) {
            requestBuilder.load(R.drawable.default_album);
        } else {
            requestBuilder.load(String.format(Constant.RESOURCE_ENDPOINT, song.getBanner()));
        }
        //用来实现高斯模糊
        //radius:模糊半径；值越大越模糊
        //sampling:采样率；值越大越模糊
        RequestOptions options = bitmapTransform(new BlurTransformation(25, 3));
        requestBuilder.apply(options).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                iv_background.setImageDrawable(resource);

                SwitchDrawableUtil switchDrawableUtil = new SwitchDrawableUtil(iv_background.getDrawable(), resource);
                iv_background.setImageDrawable(switchDrawableUtil.getDrawable());
                switchDrawableUtil.start();
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    @OnClick(R.id.ib_download)
    public void onDownloadClick() {
        System.out.println("========ib_download");
    }

    @OnClick(R.id.ib_loop_model)
    public void onLoopModelClick() {
        listManager.changeLoopModel();
        showLoopModel();

    }

    /*
     * 显示循环模式
     * */
    private void showLoopModel() {

        switch (listManager.getLoopModel()) {
            case MODEL_LOOP_RANDOM:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_random);
                break;
            case MODEL_LOOP_LIST:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_list);
                break;
            case MODEL_LOOP_ONE:
                ib_loop_model.setImageResource(R.drawable.ic_music_repeat_one);
                break;
        }

    }

    @OnClick(R.id.ib_previous)
    public void onPreviousClick() {
        listManager.play(listManager.previous());
    }

    @OnClick(R.id.ib_play)
    public void onPlayClick() {
        if (musicPlayerManager.isPlaying()) {
            listManager.pause();
        } else {
            listManager.resume();
        }

    }

    @OnClick(R.id.ib_next)
    public void onNextClick() {
        listManager.play(listManager.next());
    }

    @OnClick(R.id.ib_list)
    public void onListClick() {
        PlayListDialogFragment.show(getSupportFragmentManager());
    }

    //播放器监听回调//////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 已经暂停了
     */
    @Override
    public void onPaused(Song data) {
        ib_play.setImageResource(R.drawable.ic_music_play);
    }

    /**
     * 已经播放了
     */
    @Override
    public void onPlaying(Song data) {
        ib_play.setImageResource(R.drawable.ic_music_pause);
    }

    /**
     * 播放器准备完毕了
     */
    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        //显示初始化数据
        showInitData();
        //显示时长
        showDuration();

    }

    /**
     * 播放进度回调
     */
    @Override
    public void onProgress(Song data) {
        showProgress();
    }

    /**
     * 播放完毕
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * 歌词数据改变了
     */
    @Override
    public void onLyricChanged(Song data) {

    }

    /*
     * 显示时长
     * */
    private void showDuration() {
        long duration = listManager.getData().getDuration();
        //格式化
        tv_end.setText(TimeUtil.formatMinuteSecond((int) duration));
        //设置到进度条
        sb_progress.setMax((int) duration);
    }

    /**
     * 显示播放进度
     */
    private void showProgress() {

        long progress = listManager.getData().getProgress();
        tv_start.setText(TimeUtil.formatMinuteSecond((int) progress));
        sb_progress.setProgress((int) progress);
    }

    /*
     * 显示播放状态
     * */
    private void showMusicPlayStatus() {

        if (musicPlayerManager.isPlaying()) {
            ib_play.setImageResource(R.drawable.ic_music_pause);
        } else {
            ib_play.setImageResource(R.drawable.ic_music_play);
        }

    }
    //end播放器监听回调//////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * 删除音乐事件
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayListChangedEvent(PlayListChangedEvent event) {

        if (listManager.getDatum().size() == 0) {
            finish();
        }
    }

}
