package com.example.mycloudmusic.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.FutureTarget;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.StorageUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewActivity extends BaseCommonActivity {

    @BindView(R.id.photoView)
    PhotoView photoView;


    public static void start(Activity activity, String id, String uri) {
        Intent intent = new Intent(activity, ImagePreviewActivity.class);
        intent.putExtra(Constant.ID, id);
        intent.putExtra(Constant.URL, uri);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
    }

    @Override
    protected void initViews() {
        super.initViews();
        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        ImageUtil.showAvatar(getMainActivity(), photoView, extraString(Constant.URL));
    }

    @OnClick(R.id.bt_save)
    public void onSaveClick() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FutureTarget<File> futureTarget = Glide.with(getMainActivity()).asFile().load(extraString(Constant.URL)).submit();
                            File file = futureTarget.get();
                            String path = file.getAbsolutePath();

                            Uri uri = StorageUtil.savePicture(getMainActivity(), file);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (uri != null){
                                        ToastUtil.successShortToast("已保存");
                                    }else {
                                        ToastUtil.successShortToast("保存失败");
                                    }

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.errorShortToast("图片保存失败！");
                                }
                            });
                        }

                    }
                }
        ).start();
    }
}
