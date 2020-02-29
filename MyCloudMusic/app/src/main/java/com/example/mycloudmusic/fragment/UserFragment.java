package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.UserDetailActivity;
import com.example.mycloudmusic.adapter.UserAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

public class UserFragment extends BaseSearchFragment {

    private UserAdapter adapter;

    public static UserFragment newInstance(int position){

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.ID,position);

        UserFragment fragment = new UserFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new UserAdapter(R.layout.item_user);
        rv.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                UserDetailActivity.start(getMainActivity(),adapter.getItem(position).getId(),null);
            }
        });

    }

    @Override
    protected void fetchData(String data) {
        super.fetchData(data);

        Api.getInstance().searchUsers(data).subscribe(new HttpObserver<ListResponse<User>>() {
            @Override
            public void onSucceeded(ListResponse<User> data) {

                adapter.replaceData(data.getData());
            }
        });
    }
}
