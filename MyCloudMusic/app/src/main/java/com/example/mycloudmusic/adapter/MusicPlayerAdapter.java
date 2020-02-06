package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.fragment.MusicRecordFragment;

public class MusicPlayerAdapter extends BaseFragmentPagerAdapter<Song> {

    public MusicPlayerAdapter(Context context, FragmentManager fm) {
        super(context, fm);
    }

    @Override
    public Fragment getItem(int position) {

        return MusicRecordFragment.getInstance(getData(position));
    }
}
