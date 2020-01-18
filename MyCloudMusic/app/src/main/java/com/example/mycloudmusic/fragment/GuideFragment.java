package com.example.mycloudmusic.fragment;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;


public class GuideFragment extends BaseCommonFragment {


    private ImageView iv;

    public GuideFragment() {
    }

    public static GuideFragment newInstance(int id) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        iv = findViewById(R.id.iv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        int id = getArguments().getInt(Constant.ID);

        iv.setImageResource(id);
    }

}
