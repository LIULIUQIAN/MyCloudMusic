package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

public class UserDetailSheetFragment extends BaseCommonFragment {

    public static UserDetailSheetFragment getInstance(String userId) {
        UserDetailSheetFragment fragment = new UserDetailSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_sheet, container, false);
    }
}
