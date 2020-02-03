package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;

public class MainAdapter extends BaseFragmentPagerAdapter<Fragment> {

    /**
     * 指示器标题
     */
    private static int[] titleResources = {R.string.me, R.string.discovery, R.string.friend, R.string.video};

    public MainAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        return datum.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(titleResources[position]);
    }
}
