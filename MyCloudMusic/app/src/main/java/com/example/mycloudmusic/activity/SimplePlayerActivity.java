package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.NotificationUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SimplePlayerActivity extends BaseTitleActivity {

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


    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SimplePlayerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player);
    }

    @OnClick(R.id.bt_previous)
    public void onPreviousClick() {
        System.out.println("=============onPreviousClick");

        MusicPlayerManager m1 = MusicPlayerService.getMusicPlayerManager(getMainActivity());
        MusicPlayerManager m2 = MusicPlayerService.getMusicPlayerManager(getMainActivity());

        System.out.println("=============onPreviousClick:"+(m1==m2));
    }

    @OnClick(R.id.bt_play)
    public void onPlayClick() {
        System.out.println("=============onPlayClick");

        Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());
        NotificationUtil.showNotification(100,notification);
    }

    @OnClick(R.id.bt_next)
    public void onNextClick() {
        System.out.println("=============onNextClick");
    }

    @OnClick(R.id.bt_loop_model)
    public void onLoopClick() {
        System.out.println("=============onLoopClick");
    }
}
