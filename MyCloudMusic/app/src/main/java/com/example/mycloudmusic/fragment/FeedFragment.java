package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

public class FeedFragment extends BaseFragment {

    public static FeedFragment newInstance(String userId) {

        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(Constant.ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, null);
    }
}
