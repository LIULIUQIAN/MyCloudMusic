package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;

public class UserDetailAdapter extends BaseFragmentPagerAdapter<Fragment> {

    /**
     * 标题字符串id
     */
    private static final int[] titleIds = {R.string.music,
            R.string.feed,
            R.string.about_ta};


    public UserDetailAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        return datum.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(titleIds[position]);
    }
}
