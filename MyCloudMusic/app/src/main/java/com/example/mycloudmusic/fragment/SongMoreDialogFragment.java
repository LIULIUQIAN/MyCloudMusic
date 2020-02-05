package com.example.mycloudmusic.fragment;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.PreferenceUtil;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

public class SongMoreDialogFragment extends BaseBottomSheetDialogFragment {

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.tv_singer_name)
    TextView tv_singer_name;

    @BindView(R.id.ll_delete_song_in_sheet)
    LinearLayout ll_delete_song_in_sheet;

    @BindView(R.id.ll_collect_song)
    LinearLayout ll_collect_song;

    private Sheet sheet;
    private Song song;


    public static SongMoreDialogFragment newInstance(Sheet sheet,Song song) {

        SongMoreDialogFragment fragment = new SongMoreDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.SHEET,sheet);
        args.putSerializable(Constant.SONG,song);
        fragment.setArguments(args);
        return fragment;
    }

    public static void show(FragmentManager manager,Sheet sheet,Song song) {
        SongMoreDialogFragment fragment = newInstance(sheet,song);
        fragment.show(manager, "SongMoreDialogFragment");
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_song_more, null);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        sheet = (Sheet) getArguments().getSerializable(Constant.SHEET);
        song = (Song) getArguments().getSerializable(Constant.SONG);

        ImageUtil.showAvatar(getMainActivity(), iv_banner, sheet.getBanner());
        tv_title.setText(sheet.getTitle());
        tv_info.setText(song.getSinger().getNickname());
        tv_comment_count.setText(getResources()
                .getString(R.string.comment_count, sheet.getComments_count()));
        tv_singer_name.setText(getResources()
                .getString(R.string.singer_name, song.getSinger().getNickname()));

        if (PreferenceUtil.getInstance(getMainActivity()).getUserId().equals(sheet.getUser().getId())) {
            ll_delete_song_in_sheet.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.ll_collect_song)
    public void onCollectSongClick(){
        dismiss();

        System.out.println("===============2");
    }

    @OnClick(R.id.ll_delete_song_in_sheet)
    public void onDeleteSongInSheetClick(){
        dismiss();
        System.out.println("===============1");
    }
}
