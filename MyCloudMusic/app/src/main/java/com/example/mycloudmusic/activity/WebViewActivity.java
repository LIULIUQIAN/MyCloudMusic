package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;

public class WebViewActivity extends BaseTitleActivity {

    @BindView(R.id.wv)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initViews() {
        super.initViews();

        WebSettings webSettings = wv.getSettings();
        //运行访问文件
        webSettings.setAllowFileAccess(true);

        //支持多窗口
        webSettings.setSupportMultipleWindows(true);

        //开启js支持
        webSettings.setJavaScriptEnabled(true);

        //显示图片
        webSettings.setBlockNetworkImage(false);

        //运行显示手机中的网页
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        //运行文件访问
        webSettings.setAllowFileAccessFromFileURLs(true);

        //运行dmo存储
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //运行混合类型
            //也就说支持网页中有http/https
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        String title = getIntent().getStringExtra(Constant.TITLE);
        String url = getIntent().getStringExtra(Constant.URL);

        setTitle(title);
        wv.loadUrl(url);
    }

    public static void start(Activity activity, String title, String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra(Constant.URL, url);
        activity.startActivity(intent);
    }
}
