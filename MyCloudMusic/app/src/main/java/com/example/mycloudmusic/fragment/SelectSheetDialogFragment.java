package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SheetAdapter;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.event.OnSelectSheetEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class SelectSheetDialogFragment extends BaseBottomSheetDialogFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private List<Sheet> datum;
    private SheetAdapter adapter;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_select_sheet, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getMainActivity()));

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new SheetAdapter(R.layout.item_topic);
        recycler_view.setAdapter(adapter);

        adapter.replaceData(this.datum);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Sheet data = (Sheet) adapter.getItem(position);
                EventBus.getDefault().post(new OnSelectSheetEvent(data));
                dismiss();
            }
        });
    }

    public static SelectSheetDialogFragment getInstance() {
        SelectSheetDialogFragment fragment = new SelectSheetDialogFragment();
        return fragment;
    }

    public static void show(FragmentManager fragmentManager, List<Sheet> datum) {
        SelectSheetDialogFragment fragment = getInstance();
        fragment.setDatum(datum);
        fragment.show(fragmentManager, "SelectSheetDialogFragment");
    }

    private void setDatum(List<Sheet> datum) {
        this.datum = datum;
    }
}
