package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UserDetailAdapter extends BaseFragmentPagerAdapter<Fragment> {

    public UserDetailAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {
        return datum.get(position);
    }
}
