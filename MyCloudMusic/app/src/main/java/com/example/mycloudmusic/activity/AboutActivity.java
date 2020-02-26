package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.PackageUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseTitleActivity {

    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //获取版本名称
        String versionName = String.format("V%s",PackageUtil.getVersionName(getMainActivity()));
        tv_version.setText(versionName);

    }

    @OnClick(R.id.about_container)
    public void onAboutClick(){
        WebViewActivity.start(getMainActivity(),"关于我们","https://www.baidu.com");
    }
}
