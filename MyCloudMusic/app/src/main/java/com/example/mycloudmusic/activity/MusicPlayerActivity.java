package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.ImageUtil;

import java.time.Instant;

import butterknife.BindView;

public class MusicPlayerActivity extends BaseTitleActivity {

    @BindView(R.id.iv_background)
    ImageView iv_background;
    private ListManager listManager;
    private MusicPlayerManager musicPlayerManager;

    public static void start(Activity activity){

        Intent intent = new Intent(activity,MusicPlayerActivity.class);
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

        //显示初始化数据
        showInitData();

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
    }

    /*
    * 显示初始化数据
    * */
    private void showInitData() {

        Song song = listManager.getData();
        setTitle(song.getTitle());
        toolbar.setSubtitle(song.getSinger().getNickname());
        ImageUtil.showAvatar(getMainActivity(),iv_background,song.getBanner());

    }

}
