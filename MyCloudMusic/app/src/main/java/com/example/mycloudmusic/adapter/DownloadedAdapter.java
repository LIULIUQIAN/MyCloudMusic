package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DownloadedAdapter extends BaseFragmentPagerAdapter<Fragment> {

    /**
     * 标题数据
     */
    private static final String[] titles = {"下载完成", "正在下载"};

    public DownloadedAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

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
