package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycloudmusic.R;

public abstract class BaseFragment extends Fragment {

    /**
     * 找控件
     */
    protected void initViews(){

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return getLayoutView(inflater,container,savedInstanceState);
    }

    protected abstract View getLayoutView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
        initDatum();
    }

    public final <T extends View> T findViewById(@IdRes int id){
        return getView().findViewById(id);
    }
}
