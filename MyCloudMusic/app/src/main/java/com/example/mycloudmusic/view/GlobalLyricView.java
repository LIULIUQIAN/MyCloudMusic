package com.example.mycloudmusic.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.domain.lyric.Lyric;
import com.example.mycloudmusic.listener.GlobalLyricListener;
import com.example.mycloudmusic.util.lyric.LyricUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GlobalLyricView extends LinearLayout {

    @BindView(R.id.iv_logo)
    ImageView iv_logo;

    @BindView(R.id.llv1)
    LyricLineView llv1;

    @BindView(R.id.llv2)
    LyricLineView llv2;

    @BindView(R.id.iv_close)
    ImageView iv_close;

    @BindView(R.id.ll_play_container)
    View ll_play_container;

    @BindView(R.id.iv_play)
    ImageView iv_play;

    /**
     * 全局歌词控件监听器
     */
    private GlobalLyricListener globalLyricListener;


    public GlobalLyricView(Context context) {
        super(context);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlobalLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GlobalLyricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {

        initViews();
        initListener();
    }

    private void initViews() {

//        setBackground();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_global_lyric, this, false);
        addView(view);

        ButterKnife.bind(this);

        llv1.setLineSelected(true);

    }

    private void initListener() {

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iv_logo.getVisibility() == View.VISIBLE) {
                    simpleStyle();
                } else {
                    normalStyle();
                }

            }
        };
        this.setOnClickListener(onClickListener);

    }

    public void setGlobalLyricListener(GlobalLyricListener globalLyricListener) {
        this.globalLyricListener = globalLyricListener;
    }

    public void setBackground() {
        setBackgroundColor(getResources().getColor(R.color.global_lyric_background));
    }

    public void normalStyle() {
//        setBackground();
        iv_logo.setVisibility(VISIBLE);
        iv_close.setVisibility(VISIBLE);
        ll_play_container.setVisibility(VISIBLE);
    }

    public void simpleStyle() {
//        setBackgroundColor(getResources().getColor(R.color.transparent));
        iv_logo.setVisibility(View.GONE);
        iv_close.setVisibility(GONE);
        ll_play_container.setVisibility(GONE);
    }

    @OnClick(R.id.iv_logo)
    public void onLogoClick() {
        globalLyricListener.onLogoClick();
    }

    @OnClick(R.id.iv_close)
    public void onCloseClick() {
        globalLyricListener.onCloseClick();
    }

    @OnClick(R.id.iv_lock)
    public void onLockClick() {
        globalLyricListener.onLogoClick();
    }

    @OnClick(R.id.iv_previous)
    public void onPreviousClick() {
        globalLyricListener.onPreviousClick();
    }

    @OnClick(R.id.iv_play)
    public void onPlayClick() {
        globalLyricListener.onPlayClick();
    }


    @OnClick(R.id.iv_next)
    public void onNextClick() {
        globalLyricListener.onNextClick();
    }

    @OnClick(R.id.iv_settings)
    public void onSettingsClick() {

    }

    /**
     * 设置播放状态
     *
     * @param playing
     */
    public void setPlay(boolean playing) {
        iv_play.setImageResource(playing ? R.drawable.ic_global_pause : R.drawable.ic_global_play);

    }

    /*
    * 清楚歌词
    * */
    public void clearLyric() {

        llv1.setData(null);
        llv2.setData(null);

        llv2.setVisibility(INVISIBLE);

    }
    /**
     * 显示第二个歌词控件
     */
    public void showSecondLyricView() {
        llv2.setVisibility(View.VISIBLE);
    }

    /**
     * 设置歌词是否是精确模式
     *
     * @param accurate
     */
    public void setAccurate(boolean accurate) {
        llv1.setAccurate(accurate);
    }

    /**
     * 音乐进度回调
     *
     * @param data
     */
    public void onProgress(Song data) {

        Lyric lyric = data.getParsedLyric();

        if (lyric == null){
            return;
        }

        long progress = data.getProgress();

        Line line = LyricUtil.getLyricLine(lyric, progress);
        llv1.setData(line);
        if (lyric.isAccurate()){

            int wordIndex = LyricUtil.getWordIndex(line, progress);
            float wordPlayedTime = LyricUtil.getWordPlayedTime(line, progress);

            llv1.setLyricCurrentWordIndex(wordIndex);
            llv1.setWordPlayedTime(wordPlayedTime);
            llv1.onProgress();
        }


        int lineNumber = LyricUtil.getLineNumber(lyric, progress);
        if (lineNumber < lyric.getDatum().size() - 1){
            Line nextLine = lyric.getDatum().get(lineNumber + 1);
            llv2.setData(nextLine);
        }

    }
}