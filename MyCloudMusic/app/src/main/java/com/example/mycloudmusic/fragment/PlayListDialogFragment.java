package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.PlayListAdapter;
import com.example.mycloudmusic.domain.event.PlayListChangedEvent;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.service.MusicPlayerService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayListDialogFragment extends BaseBottomSheetDialogFragment {

    @BindView(R.id.tv_loop_model)
    TextView tv_loop_model;

    @BindView(R.id.tv_count)
    TextView tv_count;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private PlayListAdapter mAdapter;
    private ListManager listManager;

    public static PlayListDialogFragment newInstance() {

        PlayListDialogFragment fragment = new PlayListDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static void show(FragmentManager manager) {
        PlayListDialogFragment fragment = newInstance();
        fragment.show(manager, "PlayListDialogFragment");
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list, null);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        mAdapter = new PlayListAdapter(R.layout.item_play_list);
        recyclerView.setAdapter(mAdapter);

        listManager = MusicPlayerService.getListManager(getContext());

        mAdapter.replaceData(listManager.getDatum());

        tv_count.setText(String.format("(%d)", listManager.getDatum().size()));

        int index = listManager.getDatum().indexOf(listManager.getData());
        mAdapter.setSelectIndex(index);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listManager.play(listManager.getDatum().get(position));
                mAdapter.setSelectIndex(position);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                listManager.delete(position);
                mAdapter.remove(position);
                mAdapter.notifyDataSetChanged();
                tv_count.setText(String.format("(%d)", listManager.getDatum().size()));
                if (listManager.getDatum().size() ==0){
                    dismiss();
                }

                EventBus.getDefault().post(new PlayListChangedEvent());
            }
        });
    }

    @OnClick(R.id.bt_collection)
    public void onCollectionClick() {
        dismiss();
    }

    @OnClick(R.id.ib_delete_all)
    public void onDeleteAllClick() {
        dismiss();

        listManager.deleteAll();
        EventBus.getDefault().post(new PlayListChangedEvent());
    }

}
