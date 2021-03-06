package com.example.mycloudmusic.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.mycloudmusic.api.Service;
import com.example.mycloudmusic.manager.GlobalLyricManager;
import com.example.mycloudmusic.manager.impl.GlobalLyricManagerImpl;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ORMUtil;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.example.mycloudmusic.util.ServiceUtil;

import butterknife.ButterKnife;

public class BaseCommonActivity extends BaseActivity {

    protected PreferenceUtil sp;
    protected ORMUtil orm;
    private GlobalLyricManagerImpl globalLyricManager;

    @Override
    protected void onResume() {
        super.onResume();

        if (!ServiceUtil.isBackgroundRunning(getApplicationContext())){
            globalLyricManager.tryHide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ServiceUtil.isBackgroundRunning(getApplicationContext())){
            globalLyricManager.tryShow();
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        /*初始化ButterKnife*/
        ButterKnife.bind(this);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        sp = PreferenceUtil.getInstance(getMainActivity());
        orm = ORMUtil.getInstance(getMainActivity());
        globalLyricManager = GlobalLyricManagerImpl.getInstance(getApplicationContext());
    }

    /*
     * 全屏
     * */
    protected void fullScreen(){
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19){
            decorView.setSystemUiVisibility(View.GONE);
        }else if(Build.VERSION.SDK_INT >=19){
            //19及以上版本
            //SYSTEM_UI_FLAG_HIDE_NAVIGATION:隐藏导航栏
            //SYSTEM_UI_FLAG_IMMERSIVE_STICKY:从状态栏下拉会半透明悬浮显示一会儿状态栏和导航栏
            //SYSTEM_UI_FLAG_FULLSCREEN:全屏
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(options);

        }
    }
    /*
    * 隐藏状态栏
    * */
    protected void hideStatusBar(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 状态栏文字显示白色
     * 内容显示到状态栏下面
     */
    protected void lightStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            //设置状态栏背景颜色为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //去除半透明效果(如果有)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让内容显示到状态栏
            //SYSTEM_UI_FLAG_LAYOUT_STABLE：状态栏文字显示白色
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR：状态栏文字显示黑色
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }
    }
    /**
     * 启动界面
     * @param clazz
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(getMainActivity(),clazz);
        startActivity(intent);
    }
    /**
     * 启动界面并关闭当前界面
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz){
        startActivity(clazz);
        finish();
    }

    /**
     * 启动界面，可以传递一个字符串参数
     */
    protected void startActivityExtraId(Class<?> clazz, String id) {
        Intent intent = new Intent(getMainActivity(), clazz);
        if (!TextUtils.isEmpty(id)) {
            intent.putExtra(Constant.ID, id);
        }
        startActivity(intent);
    }

    /**
     * 获取字符串类型Id
     */
    protected String extraId() {
        return extraString(Constant.ID);
    }

    /**
     * 获取字符串
     */
    protected String extraString(String key) {
        return getIntent().getStringExtra(key);
    }

    /**
     * 获取界面方法
     * @return
     */
    protected BaseCommonActivity getMainActivity() {
        return this;
    }

}
