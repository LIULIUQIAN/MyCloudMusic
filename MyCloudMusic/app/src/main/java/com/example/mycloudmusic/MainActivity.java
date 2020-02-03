package com.example.mycloudmusic;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.activity.BaseTitleActivity;
import com.example.mycloudmusic.activity.SettingActivity;
import com.example.mycloudmusic.activity.WebViewActivity;
import com.example.mycloudmusic.adapter.MainAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.fragment.DiscoveryFragment;
import com.example.mycloudmusic.fragment.FeedFragment;
import com.example.mycloudmusic.fragment.MeFragment;
import com.example.mycloudmusic.fragment.VideoFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseTitleActivity {

    //抽屉 view
    @BindView(R.id.dl)
    DrawerLayout dl;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_description)
    TextView tv_description;

    //滑动view
    @BindView(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //处理动作
        processIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        processIntent(intent);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //侧滑配置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        fetchUserData();

        //缓存页面数量
        vp.setOffscreenPageLimit(4);

        MainAdapter adapter = new MainAdapter(this, getSupportFragmentManager());
        vp.setAdapter(adapter);

        List<Fragment> pageList = new ArrayList<>();
        pageList.add(MeFragment.newInstance());
        pageList.add(DiscoveryFragment.newInstance());
        pageList.add(FeedFragment.newInstance());
        pageList.add(VideoFragment.newInstance());
        adapter.setDatum(pageList);
    }

    /*
     * 请求数据
     * */
    private void fetchUserData() {

        Api.getInstance()
                .userDetail(sp.getUserId())
                .subscribe(new HttpObserver<DetailResponse<User>>() {
                    @Override
                    public void onSucceeded(DetailResponse<User> data) {
                        setUserInfo(data.getData());
                    }
                });

    }

    /*
     * 设置用户信息
     * */
    private void setUserInfo(User data) {
        ImageUtil.showCircleAvatar(this, iv_avatar, data.getAvatar());
        tv_nickname.setText(data.getNickname());
        tv_description.setText(data.getDescriptionFormat());
    }

    /*
     * 处理动作
     * */
    private void processIntent(Intent intent) {

        //广告点击
        if (Constant.ACTION_AD.equals(intent.getAction())) {
            WebViewActivity.start(getMainActivity(), "广告详情", intent.getStringExtra(Constant.URL));
        }
    }

    /*
     * 用户信息点击
     * */
    @OnClick(R.id.ll_user)
    public void onUserClick() {
        System.out.println("=============用户信息点击");
    }

    /*
     * 点击设置
     * */
    @OnClick(R.id.ll_setting)
    public void onSettingClick() {
        startActivity(SettingActivity.class);
        closeDrawer();
    }

    private void closeDrawer() {
        dl.closeDrawer(GravityCompat.START);
    }
}
