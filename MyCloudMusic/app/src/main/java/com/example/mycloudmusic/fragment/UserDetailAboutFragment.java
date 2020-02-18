package com.example.mycloudmusic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;

public class UserDetailAboutFragment extends BaseCommonFragment {

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_gender)
    TextView tv_gender;

    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    @BindView(R.id.tv_area)
    TextView tv_area;

    @BindView(R.id.tv_description)
    TextView tv_description;

    public static UserDetailAboutFragment getInstance(String userId) {

        UserDetailAboutFragment fragment = new UserDetailAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail_about, container, false);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Api.getInstance().userDetail(extraId()).subscribe(new HttpObserver<DetailResponse<User>>() {
            @Override
            public void onSucceeded(DetailResponse<User> data) {

                next(data.getData());
            }
        });
    }

    private void next(User data){

        tv_nickname.setText(getResources().getString(R.string.nickname_value,data.getNickname()));
        tv_gender.setText(getResources().getString(R.string.gender_value,data.getGenderFormat()));
        tv_birthday.setText(getResources().getString(R.string.birthday_value,data.getBirthday()));
        tv_area.setText(getResources().getString(R.string.area_value,data.getArea()));
        tv_description.setText(getResources().getString(R.string.description_value,data.getDescription()));

    }
}
