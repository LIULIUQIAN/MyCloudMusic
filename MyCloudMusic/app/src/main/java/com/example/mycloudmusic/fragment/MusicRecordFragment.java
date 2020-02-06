package com.example.mycloudmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.util.Constant;

public class MusicRecordFragment extends BaseCommonFragment {
    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_music, null);
    }

    public static MusicRecordFragment getInstance(Song song) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.DATA, song);
        MusicRecordFragment fragment = new MusicRecordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
