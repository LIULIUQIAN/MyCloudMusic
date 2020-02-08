package com.example.mycloudmusic.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;

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

        ImageUtil.showAvatar(getMainActivity(),photoView,extraString(Constant.URL));
    }
}
