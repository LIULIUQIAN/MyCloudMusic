package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.fragment.GuideFragment;


public class GuideAdapter extends BaseFragmentPagerAdapter<Integer> {


    public GuideAdapter(Context context, FragmentManager fm) {
        super(context,fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(getData(position));
    }

}
