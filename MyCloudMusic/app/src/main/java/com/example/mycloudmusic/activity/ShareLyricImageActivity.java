package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.util.BitmapUtil;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.ShareUtil;
import com.example.mycloudmusic.util.StorageUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.example.mycloudmusic.util.ViewUtil;

import java.io.File;

import butterknife.BindView;

public class ShareLyricImageActivity extends BaseTitleActivity {

    @BindView(R.id.lyric_container)
    View lyric_container;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.tv_lyric)
    TextView tv_lyric;

    @BindView(R.id.tv_song)
    TextView tv_song;

    public static void start(Activity activity, Song data, String lyric) {

        Intent intent = new Intent(activity, ShareLyricImageActivity.class);
        intent.putExtra(Constant.DATA, data);
        intent.putExtra(Constant.SONG, lyric);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lyric_image);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Song data = (Song) getIntent().getSerializableExtra(Constant.DATA);
        String lyric = extraString(Constant.SONG);

        ImageUtil.showAvatar(getMainActivity(), iv_banner, data.getBanner());
        tv_lyric.setText(lyric);
        tv_song.setText(getResources().getString(R.string.share_song_name, data.getSinger().getNickname(), data.getTitle()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music_player, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            onShareClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onShareClick() {

        Bitmap bitmap = ViewUtil.captureBitmap(lyric_container);

        File file = StorageUtil.getExternalPath(getMainActivity(), sp.getUserId(), "share", StorageUtil.JPG);

        //保存图片到文件
        BitmapUtil.saveToFile(bitmap, file);

        //将私有路径的图片保存到相册
        //这样其他应用才能访问
        Uri uri = StorageUtil.savePicture(getMainActivity(), file);
        if (uri != null) {
            //获取uri文件路径
            String path = StorageUtil.getMediaStorePath(getMainActivity(), uri);

            //分享图片
            ShareUtil.shareImage(getMainActivity(), path);
        } else {
            ToastUtil.errorShortToast(R.string.error_share_failed);
        }

    }
}
