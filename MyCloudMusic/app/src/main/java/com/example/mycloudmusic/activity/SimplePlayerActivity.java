package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.NotificationUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SimplePlayerActivity extends BaseTitleActivity implements MusicPlayerListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.tv_start)
    TextView tv_start;

    @BindView(R.id.sb_progress)
    SeekBar sb_progress;

    @BindView(R.id.tv_end)
    TextView tv_end;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.bt_play)
    Button bt_play;

    private MusicPlayerManager musicPlayerManager;
    private ListManager listManager;


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SimplePlayerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
    }

    /*
     * 界面可见
     * */
    @Override
    protected void onResume() {
        super.onResume();

        musicPlayerManager.addMusicPlayerListener(this);
        //显示音乐时长
        showDuration();
        //显示播放进度
        showProgress();
    }

    /*
     * 界面不可见
     * */
    @Override
    protected void onPause() {
        super.onPause();

        musicPlayerManager.removeMusicPlayerListener(this);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        musicPlayerManager.addMusicPlayerListener(this);

        listManager = MusicPlayerService.getListManager(getApplicationContext());

//        String songUrl = "http://dev-courses-misuc.ixuea.com/assets/wangbiliaodewenrou_andongyang.mp3";
//        Song song = new Song();
//        song.setUri(songUrl);
//        musicPlayerManager.play(songUrl, song);

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        sb_progress.setOnSeekBarChangeListener(this);
    }

    @OnClick(R.id.bt_previous)
    public void onPreviousClick() {
        Song song = listManager.previous();
        listManager.play(song);
    }

    @OnClick(R.id.bt_play)
    public void onPlayClick() {
        if (musicPlayerManager.isPlaying()) {
            listManager.pause();
        } else {
            listManager.resume();
        }
    }

    @OnClick(R.id.bt_next)
    public void onNextClick() {

        Song song = listManager.next();
        listManager.play(song);
    }

    @OnClick(R.id.bt_loop_model)
    public void onLoopClick() {

    }

    //音乐播放器回调
    @Override
    public void onPaused(Song data) {
        bt_play.setText("播放");
    }

    @Override
    public void onPlaying(Song data) {
        bt_play.setText("暂停");
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {

        showDuration();
    }

    @Override
    public void onProgress(Song data) {
        showProgress();
    }

    /*
     * 显示时长
     * */
    private void showDuration() {

        long duration = musicPlayerManager.getData().getDuration();
        tv_end.setText(TimeUtil.formatMinuteSecond((int) duration));
        sb_progress.setMax((int) duration);

        tv_title.setText(musicPlayerManager.getData().getTitle());
    }

    /**
     * 显示播放进度
     */
    private void showProgress() {
        long progress = musicPlayerManager.getData().getProgress();
        sb_progress.setProgress((int) progress);
        tv_start.setText(TimeUtil.formatMinuteSecond((int) progress));
    }

    //end音乐播放器回调


    //SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (fromUser) {
            musicPlayerManager.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //end SeekBar
}
