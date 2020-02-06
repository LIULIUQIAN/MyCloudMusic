package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SimplePlayerAdapter;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.NotificationUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_LIST;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_ONE;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_RANDOM;

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

    @BindView(R.id.bt_loop_model)
    Button bt_loop_model;

    private MusicPlayerManager musicPlayerManager;
    private ListManager listManager;
    private SimplePlayerAdapter adapter;


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
        //循环模式
        showLoopModel();

        scrollPosition();
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
    protected void initViews() {
        super.initViews();

        recycler_view.setHasFixedSize(true);

        recycler_view.setLayoutManager(new LinearLayoutManager(getMainActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        musicPlayerManager.addMusicPlayerListener(this);

        listManager = MusicPlayerService.getListManager(getApplicationContext());

        adapter = new SimplePlayerAdapter(android.R.layout.simple_list_item_1);
        recycler_view.setAdapter(adapter);
        adapter.replaceData(listManager.getDatum());

        //列表滑动删除
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return isViewCreateByAdapter(viewHolder)
                        ? makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.ACTION_STATE_IDLE)
                        : makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view);
        adapter.enableSwipeItem();

        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {

            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                listManager.delete(i);
                if (listManager.getDatum().size() == 0) {
                    finish();
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {

            }
        };
        adapter.setOnItemSwipeListener(onItemSwipeListener);
    }

    private boolean isViewCreateByAdapter(@NonNull RecyclerView.ViewHolder viewHolder) {
        int type = viewHolder.getItemViewType();
        return type == 273 || type == 546 || type == 819 || type == 1365;
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        sb_progress.setOnSeekBarChangeListener(this);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Song song = listManager.getDatum().get(position);
                listManager.play(song);
            }
        });

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

        listManager.changeLoopModel();
        showLoopModel();
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
        scrollPosition();
    }

    @Override
    public void onProgress(Song data) {
        showProgress();
    }

    /*
     * 显示时长
     * */
    private void showDuration() {

        long duration = listManager.getData().getDuration();
        tv_end.setText(TimeUtil.formatMinuteSecond((int) duration));
        sb_progress.setMax((int) duration);

        tv_title.setText(listManager.getData().getTitle());
    }

    /**
     * 显示播放进度
     */
    private void showProgress() {
        long progress = listManager.getData().getProgress();
        sb_progress.setProgress((int) progress);
        tv_start.setText(TimeUtil.formatMinuteSecond((int) progress));
    }

    /**
     * 显示循环模式
     */
    private void showLoopModel() {

        int model = listManager.getLoopModel();
        switch (model) {
            case MODEL_LOOP_LIST:
                bt_loop_model.setText("列表循环");
                break;
            case MODEL_LOOP_ONE:
                bt_loop_model.setText("单曲循环");
                break;
            case MODEL_LOOP_RANDOM:
                bt_loop_model.setText("随机循环");
                break;
        }

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

    /**
     * 选中当前音乐
     */
    private void scrollPosition() {

        recycler_view.post(new Runnable() {
            @Override
            public void run() {
                int index = listManager.getDatum().indexOf(listManager.getData());
                recycler_view.smoothScrollToPosition(index);
                adapter.setSelectIndex(index);
            }
        });
    }

}
