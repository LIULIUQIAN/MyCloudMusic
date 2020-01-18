package com.example.mycloudmusic.activity;

import android.content.Intent;

public class BaseCommonActivity extends BaseActivity {

    /**
     * 启动界面
     * @param clazz
     */
    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(getMainActivity(),GuideActivity.class);
        startActivity(intent);
        finish();
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
     * 获取界面方法
     * @return
     */
    protected BaseCommonActivity getMainActivity() {
        return this;
    }

}
