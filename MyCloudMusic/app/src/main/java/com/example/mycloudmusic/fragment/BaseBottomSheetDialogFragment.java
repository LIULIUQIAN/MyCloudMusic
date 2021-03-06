package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.domain.Sheet;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment {

    /**
     * 找控件
     */
    protected void initViews() {

    }

    /**
     * 设置数据
     */
    protected void initDatum() {

    }

    /**
     * 设置监听器
     */
    protected void initListeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);

        return view;
    }

    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initDatum();
        initListeners();
    }

    /**
     * 获取activity
     *
     * @return
     */
    protected BaseCommonActivity getMainActivity() {
        return (BaseCommonActivity) getActivity();
    }


}
