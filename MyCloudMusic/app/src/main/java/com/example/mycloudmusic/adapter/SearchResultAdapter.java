package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SearchResultAdapter extends BaseFragmentPagerAdapter<Fragment> {

    /**
     * 标题数据
     */
    private CharSequence[] titles = {"歌单", "用户"};

    public SearchResultAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return datum.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
