package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.LyricAdapter;
import com.example.mycloudmusic.adapter.MusicPlayerAdapter;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.domain.event.OnPlayEvent;
import com.example.mycloudmusic.domain.event.OnRecordClickEvent;
import com.example.mycloudmusic.domain.event.OnStartRecordEvent;
import com.example.mycloudmusic.domain.event.OnStopRecordEvent;
import com.example.mycloudmusic.domain.event.PlayListChangedEvent;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.domain.lyric.Lyric;
import com.example.mycloudmusic.fragment.PlayListDialogFragment;
import com.example.mycloudmusic.listener.DownloadListener;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.DensityUtil;
import com.example.mycloudmusic.util.FileUtil;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.StorageUtil;
import com.example.mycloudmusic.util.SwitchDrawableUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.example.mycloudmusic.util.lyric.LyricUtil;
import com.example.mycloudmusic.view.LyricLineView;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.domain.DownloadInfo;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.SoftReference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_LIST;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_ONE;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_RANDOM;
import static com.example.mycloudmusic.util.Constant.THUMB_DURATION;
import static com.example.mycloudmusic.util.Constant.THUMB_ROTATION_PAUSE;
import static com.example.mycloudmusic.util.Constant.THUMB_ROTATION_PLAY;

public class MusicPlayerActivity extends BaseTitleActivity implements MusicPlayerListener {

    @BindView(R.id.iv_background)
    ImageView iv_background;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.cl_record)
    View cl_record;

    @BindView(R.id.rl_lyric)
    View rl_lyric;

    @BindView(R.id.tv_lyric_time)
    TextView tv_lyric_time;

    @BindView(R.id.ll_lyric_drag)
    View ll_lyric_drag;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.iv_record_thumb)
    ImageView iv_record_thumb;

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

    @BindView(R.id.ib_download)
    ImageButton ib_download;


    private ListManager listManager;
    private MusicPlayerManager musicPlayerManager;
    private MusicPlayerAdapter recordAdapter;
    private ObjectAnimator playThumbAnimator;
    private ValueAnimator pauseThumbAnimator;
    private LyricAdapter lyricAdapter;
    private int lineNumber;
    private int lyricOffsetX;
    private LinearLayoutManager layoutManager;

    /**
     * 歌词填充多个占位数据
     */
    private int lyricPlaceholderSize;
    private boolean isDrag;
    private TimerTask lyricTimerTask;
    private Timer lyricTimer;

    private Line scrollSelectedLyricLine;
    private DownloadManager downloadManager;
    private DownloadInfo downloadInfo;

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

        //滚动到当前音乐位置
        scrollPosition();

        //显示歌词
        showLyricData();


    }

    /*
     * 滚动到当前音乐位置
     * */
    private void scrollPosition() {
        viewPager.post(new Runnable() {
            @Override
            public void run() {

                int index = listManager.getDatum().indexOf(listManager.getData());
                viewPager.setCurrentItem(index, false);

                if (musicPlayerManager.isPlaying()) {
                    startRecordRotate();
                } else {
                    stopRecordRotate();
                }
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViews() {
        super.initViews();

        lightStatusBar();

        viewPager.setOffscreenPageLimit(3);

        //黑胶唱片指针旋转点
        int rotate = DensityUtil.dipTopx(getMainActivity(), 15);
        iv_record_thumb.setPivotX(rotate);
        iv_record_thumb.setPivotY(rotate);

        recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getMainActivity());
        recycler_view.setLayoutManager(layoutManager);

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        listManager = MusicPlayerService.getListManager(getApplicationContext());
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());

        downloadManager = AppContext.getInstance().getDownloadManager();

        recordAdapter = new MusicPlayerAdapter(getMainActivity(), getSupportFragmentManager());
        viewPager.setAdapter(recordAdapter);
        recordAdapter.setDatum(listManager.getDatum());

        //指针动画创建
        playThumbAnimator = ObjectAnimator.ofFloat(iv_record_thumb, "rotation", THUMB_ROTATION_PAUSE, THUMB_ROTATION_PLAY);
        playThumbAnimator.setDuration(THUMB_DURATION);

        pauseThumbAnimator = ValueAnimator.ofFloat(THUMB_ROTATION_PLAY, THUMB_ROTATION_PAUSE);
        pauseThumbAnimator.setDuration(THUMB_DURATION);
        pauseThumbAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                iv_record_thumb.setRotation((Float) animation.getAnimatedValue());
            }
        });

        lyricAdapter = new LyricAdapter(R.layout.item_lyric);
        recycler_view.setAdapter(lyricAdapter);

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            /**
             * 滚动状态改变了
             *
             * @param state 滚动状态
             * @see ViewPager#SCROLL_STATE_IDLE：空闲
             * @see ViewPager#SCROLL_STATE_DRAGGING：正在拖拽
             * @see ViewPager#SCROLL_STATE_SETTLING：滚动完成后
             */
            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == SCROLL_STATE_DRAGGING) {
                    stopRecordRotate();

                } else if (state == SCROLL_STATE_IDLE) {
                    Song song = listManager.getDatum().get(viewPager.getCurrentItem());
                    if (song.getId().equals(listManager.getData().getId())) {

                        if (musicPlayerManager.isPlaying()) {
                            startRecordRotate();
                        }
                    } else {
                        listManager.play(song);
                    }
                }

            }

        });

        lyricAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                cl_record.setVisibility(View.VISIBLE);
                rl_lyric.setVisibility(View.GONE);
            }
        });

        lyricAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                SelectLyricActivity.start(getMainActivity(), listManager.getData());
                return true;
            }
        });

        //添加布局监听器
        viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lyricOffsetX = viewPager.getHeight() / 2 - DensityUtil.dipTopx(getMainActivity(), 40) / 2;
                viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == SCROLL_STATE_DRAGGING) {
                    //拖拽状态
                    showDragView();
                } else if (newState == SCROLL_STATE_IDLE) {
                    //空闲状态
                    prepareScrollLyricView();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() + lyricPlaceholderSize;

                if (isDrag) {
                    Object data = lyricAdapter.getItem(firstVisibleItemPosition);
                    if (data instanceof String) {
                        if (firstVisibleItemPosition < lyricPlaceholderSize) {
                            scrollSelectedLyricLine = (Line) lyricAdapter.getItem(lyricPlaceholderSize);
                        } else {
                            int index = lyricAdapter.getItemCount() - 1 - lyricPlaceholderSize;
                            scrollSelectedLyricLine = (Line) lyricAdapter.getItem(index);
                        }
                    } else {
                        scrollSelectedLyricLine = (Line) data;
                    }

                    tv_lyric_time.setText(TimeUtil.formatMinuteSecond((int) scrollSelectedLyricLine.getStartTime()));
                }
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

        downloadInfo = downloadManager.getDownloadById(song.getId());
        if (downloadInfo != null) {
            //设置下载回调
            setDownloadCallback();
        }
        refresh();
    }

    /**
     * 刷新下载状态
     */
    private void refresh() {

        if (downloadInfo != null) {
            switch (downloadInfo.getStatus()) {
                case DownloadInfo.STATUS_COMPLETED:
                    ib_download.setImageResource(R.drawable.ic_downloaded);
                    break;
                default:
                    ib_download.setImageResource(R.drawable.ic_download);
                    break;
            }

            //打印下载进度
            String start = FileUtil.formatFileSize(downloadInfo.getProgress());
            String size = FileUtil.formatFileSize(downloadInfo.getSize());

            Log.d("downloadInfo", "refresh:" + start + "," + size);
        } else {
            ib_download.setImageResource(R.drawable.ic_download);
        }

    }

    /**
     * 设置下载回调
     */
    private void setDownloadCallback() {
        downloadInfo.setDownloadListener(new DownloadListener(new SoftReference(this)) {
            @Override
            public void onRefresh() {

                if (getUserTag() != null && getUserTag().get()!= null){
                    MusicPlayerActivity activity = (MusicPlayerActivity) getUserTag().get();
                    activity.refresh();
                }

            }
        });
    }

    @OnClick(R.id.ib_download)
    public void onDownloadClick() {

        if (downloadInfo != null){

            if (downloadInfo.getStatus() == DownloadInfo.STATUS_COMPLETED){
                ToastUtil.successShortToast("该歌曲已经下载了!");
            }else {
                ToastUtil.successShortToast("已经在下载列表中了!");
            }
        }else {
            createDownload();
        }
    }
    /**
     * 创建下载任务
     */
    private void createDownload() {

        Song data = listManager.getData();
        String urlString = String.format(Constant.RESOURCE_ENDPOINT, data.getUri());

        String path = StorageUtil.getExternalPath(getMainActivity(), sp.getUserId(), data.getTitle(), StorageUtil.MP3).getAbsolutePath();
        Log.d("createDownload", "createDownload:" + path);

        downloadInfo = new DownloadInfo.Builder()
                .setId(data.getId())
                .setUrl(urlString)
                .setPath(path)
                .build();
        downloadInfo.setCreateAt(System.currentTimeMillis());
        setDownloadCallback();
        downloadManager.download(downloadInfo);
        orm.saveSong(data);
        ToastUtil.successShortToast("下载任务添加成功!");

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

    @OnClick(R.id.ib_lyric_play)
    public void onLyricPlayClick() {

        cancelLyricTask();
        listManager.seekTo((int) scrollSelectedLyricLine.getStartTime());
        enableScrollLyric();
    }

    //播放器监听回调//////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 已经暂停了
     */
    @Override
    public void onPaused(Song data) {
        ib_play.setImageResource(R.drawable.ic_music_play);
        stopRecordRotate();
    }

    /**
     * 已经播放了
     */
    @Override
    public void onPlaying(Song data) {
        ib_play.setImageResource(R.drawable.ic_music_pause);

        startRecordRotate();
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
        //滚动到当前音乐位置
        scrollPosition();

    }

    /**
     * 播放进度回调
     */
    @Override
    public void onProgress(Song data) {
        showProgress();

        //显示歌词进度
        showLyricProgress(listManager.getData().getProgress());
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

        showLyricData();

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

    /**
     * 播放前回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayEvent(OnPlayEvent event) {
        stopRecordRotate();
    }

    /*
     * 黑胶唱片点击
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecordClickEvent(OnRecordClickEvent event) {

        if (isLyricEmpty()) {
            return;
        }

        cl_record.setVisibility(View.GONE);
        rl_lyric.setVisibility(View.VISIBLE);
    }

    /**
     * 是否没有歌词数据
     */
    private boolean isLyricEmpty() {
        return lyricAdapter.getItemCount() == 0;
    }

    /**
     * 黑胶唱片停止滚动
     * 黑胶唱片指针回到暂停状态
     */
    private void stopRecordRotate() {

        float thumbRotation = iv_record_thumb.getRotation();
        if (thumbRotation != THUMB_ROTATION_PAUSE) {
            pauseThumbAnimator.start();
        }

        Song data = listManager.getData();
        EventBus.getDefault().post(new OnStopRecordEvent(data));

    }

    /**
     * 开始黑胶唱片滚动
     * 指针回到播放位置
     */
    private void startRecordRotate() {
        playThumbAnimator.start();

        Song data = listManager.getData();
        EventBus.getDefault().post(new OnStartRecordEvent(data));
    }

    /**
     * 显示歌词数据
     */
    private void showLyricData() {

        if (lyricPlaceholderSize > 0) {
            next();
        }
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                int height = viewPager.getMeasuredHeight();
                lyricPlaceholderSize = (int) Math.ceil(height * 1.0 / 2 / DensityUtil.dipTopx(getMainActivity(), 40));

                next();
            }
        });


    }

    private void next() {
        Song song = listManager.getData();
        if (song.getParsedLyric() == null) {
            lyricAdapter.replaceData(new ArrayList<>());
        } else {

            List<Object> datum = new ArrayList<>();
            addLyricFillData(datum);
            datum.addAll(song.getParsedLyric().getDatum());
            addLyricFillData(datum);
            lyricAdapter.setAccurate(song.getParsedLyric().isAccurate());
            lyricAdapter.replaceData(datum);
        }
    }

    /*
     * 显示歌词进度
     * */
    private void showLyricProgress(long progress) {

        Lyric data = listManager.getData().getParsedLyric();
        if (data == null || lyricAdapter.getData() == null || lyricAdapter.getData().size() == 0) {
            return;
        }

        if (isDrag) {
            return;
        }

        int newLineNumber = LyricUtil.getLineNumber(data, progress) + lyricPlaceholderSize;

        if (newLineNumber != lineNumber) {
            //滚动到当前行
            scrollLyricPosition(newLineNumber);
            lineNumber = newLineNumber;
        }

        if (data.isAccurate()) {
            Object object = lyricAdapter.getData().get(lineNumber);
            if (object instanceof Line) {
                Line line = (Line) object;

                int lyricCurrentWordIndex = LyricUtil.getWordIndex(line, progress);

                float wordPlayedTime = LyricUtil.getWordPlayedTime(line, progress);

                View view = layoutManager.findViewByPosition(lineNumber);

                if (view != null) {
                    LyricLineView llv = view.findViewById(R.id.llv);
                    llv.setLyricCurrentWordIndex(lyricCurrentWordIndex);
                    llv.setWordPlayedTime(wordPlayedTime);
                    llv.onProgress();
                }

            }
        }

    }

    private void scrollLyricPosition(int lineNumber) {

        recycler_view.post(new Runnable() {
            @Override
            public void run() {
                lyricAdapter.setSelectedIndex(lineNumber);
//                recycler_view.smoothScrollToPosition(lineNumber);

                layoutManager.scrollToPositionWithOffset(lineNumber, lyricOffsetX);
            }
        });
    }

    /**
     * 添加歌词占位数据
     */
    public void addLyricFillData(List<Object> datum) {
        for (int i = 0; i < lyricPlaceholderSize; i++) {
            datum.add("fill");
        }
    }

    /**
     * 歌词空闲状态
     */
    private void prepareScrollLyricView() {

        cancelLyricTask();

        lyricTimerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        enableScrollLyric();
                    }
                });
            }
        };

        lyricTimer = new Timer();
        lyricTimer.schedule(lyricTimerTask, Constant.LYRIC_HIDE_DRAG_TIME);
    }

    private void cancelLyricTask() {

        if (lyricTimerTask != null) {
            lyricTimerTask.cancel();
            lyricTimerTask = null;

        }
        if (lyricTimer != null) {
            lyricTimer.cancel();
            lyricTimer = null;
        }

    }

    /*
     * 歌词拖拽状态
     * */
    private void showDragView() {

        ll_lyric_drag.setVisibility(View.VISIBLE);
        isDrag = true;
    }

    private void enableScrollLyric() {
        ll_lyric_drag.setVisibility(View.GONE);
        isDrag = false;
    }

}
