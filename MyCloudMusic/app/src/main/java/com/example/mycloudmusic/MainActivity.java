package com.example.mycloudmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.activity.BaseMusicPlayerActivity;
import com.example.mycloudmusic.activity.CodeActivity;
import com.example.mycloudmusic.activity.MusicPlayerActivity;
import com.example.mycloudmusic.activity.ScanActivity;
import com.example.mycloudmusic.activity.SettingActivity;
import com.example.mycloudmusic.activity.ShopActivity;
import com.example.mycloudmusic.activity.SimplePlayerActivity;
import com.example.mycloudmusic.activity.UserActivity;
import com.example.mycloudmusic.activity.UserDetailActivity;
import com.example.mycloudmusic.activity.WebViewActivity;
import com.example.mycloudmusic.adapter.MainAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.UserResult;
import com.example.mycloudmusic.domain.UserTest;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.fragment.DiscoveryFragment;
import com.example.mycloudmusic.fragment.FeedFragment;
import com.example.mycloudmusic.fragment.MeFragment;
import com.example.mycloudmusic.fragment.VideoFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.DataUtil;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.PinyinUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mycloudmusic.util.Constant.REQUEST_OVERLAY_PERMISSION;

public class MainActivity extends BaseMusicPlayerActivity {

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

    //指示器
    @BindView(R.id.magic_indicator)
    MagicIndicator magic_indicator;


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
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestDrawOverlays();
        }

    }

    @Override
    protected void initViews() {
        super.initViews();

        //侧滑配置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        //缓存页面数量
        vp.setOffscreenPageLimit(4);

        MainAdapter adapter = new MainAdapter(this, getSupportFragmentManager());
        vp.setAdapter(adapter);

        List<Fragment> pageList = new ArrayList<>();
        pageList.add(MeFragment.newInstance());
        pageList.add(DiscoveryFragment.newInstance());
        pageList.add(FeedFragment.newInstance(null));
        pageList.add(VideoFragment.newInstance());
        adapter.setDatum(pageList);

        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return pageList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {

                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setNormalColor(getResources().getColor(R.color.tab_normal));
                titleView.setSelectedColor(getResources().getColor(R.color.white));
                titleView.setText(adapter.getPageTitle(index));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vp.setCurrentItem(index, false);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });

        magic_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magic_indicator, vp);
        //是否自动调整
        commonNavigator.setAdjustMode(true);

        vp.setCurrentItem(1, false);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        fetchUserData();
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                fetchUserData();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
        } else if (Constant.ACTION_MUSIC_PLAY_CLICK.equals(intent.getAction())) {
            //音乐通知点击
            MusicPlayerActivity.start(getMainActivity());
        }
    }

    /*
     * 用户信息点击
     * */
    @OnClick(R.id.ll_user)
    public void onUserClick() {
        startActivityExtraId(UserDetailActivity.class, sp.getUserId());
        closeDrawer();
    }

    /*
     * 我的二维码点击
     * */
    @OnClick(R.id.ll_code)
    public void onCodeClick() {
        startActivityExtraId(CodeActivity.class, sp.getUserId());
        closeDrawer();
    }

    /*
     * 扫一扫码点击
     * */
    @OnClick(R.id.ll_scan)
    public void onScanClick() {
        startActivity(ScanActivity.class);
        closeDrawer();
    }


    /*
     * 好友点击
     * */
    @OnClick(R.id.ll_friend)
    public void onFriendClick() {
        UserActivity.start(getMainActivity(), sp.getUserId(), UserActivity.FRIEND);
        closeDrawer();
    }

    /*
     * 粉丝点击
     * */
    @OnClick(R.id.ll_fans)
    public void onFansClick() {
        UserActivity.start(getMainActivity(), sp.getUserId(), UserActivity.FANS);
        closeDrawer();
    }

    /*
     * 商城点击
     * */
    @OnClick(R.id.ll_shop)
    public void onShopClick() {
        startActivity(ShopActivity.class);
        closeDrawer();
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

    /**
     * 检查是否有悬浮窗权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean requestDrawOverlays() {

        if (!Settings.canDrawOverlays(getMainActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getMainActivity().getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_OVERLAY_PERMISSION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (requestDrawOverlays()) {
                        Log.e("onActivityResult", "权限获取成功");
                    } else {
                        Log.e("onActivityResult", "权限获取失败");
                    }
                }
                break;
        }
    }
}
