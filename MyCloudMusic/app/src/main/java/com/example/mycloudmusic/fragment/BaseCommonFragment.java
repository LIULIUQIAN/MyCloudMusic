package com.example.mycloudmusic.fragment;

import android.content.Intent;
import android.view.View;

import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.util.PreferenceUtil;

import butterknife.ButterKnife;

public abstract class BaseCommonFragment extends BaseFragment {

    /**
     * 偏好设置工具类
     */
    protected PreferenceUtil sp;

    @Override
    protected void initViews() {
        super.initViews();
        /*初始化ButterKnife*/
        ButterKnife.bind(this, getView());
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //初始化偏好设置工具类
        sp = PreferenceUtil.getInstance(getMainActivity());

    }

    /**
     * 启动界面
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getMainActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 启动界面并关闭当前界面
     *
     * @param clazz
     */
    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(clazz);
        getMainActivity().finish();
    }

    /**
     * 获取界面方法
     *
     * @return
     */
    protected BaseCommonActivity getMainActivity() {
        return (BaseCommonActivity) getActivity();
    }

}
