package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.UserDetailAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.fragment.FeedFragment;
import com.example.mycloudmusic.fragment.UserDetailAboutFragment;
import com.example.mycloudmusic.fragment.UserDetailSheetFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends BaseTitleActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.iv_avatar)
    CircleImageView iv_avatar;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.bt_follow)
    Button bt_follow;

    @BindView(R.id.bt_send_message)
    Button bt_send_message;

    @BindView(R.id.btn_layout)
    LinearLayout btnLayout;


    private String id;
    private String nickname;
    private User data;
    private UserDetailAdapter adapter;

    public static void start(Activity activity, String id, String nickname) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        if (!TextUtils.isEmpty(id)) {
            intent.putExtra(Constant.ID, id);
        }
        if (!TextUtils.isEmpty(nickname)) {
            intent.putExtra(Constant.NICKNAME, nickname);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        id = extraId();
        nickname = extraString(Constant.NICKNAME);
        if (TextUtils.isEmpty(id)) {
            id = "-1";
        }

        feachData();
    }

    /**
     * 请求用户信息数据
     */
    private void feachData() {
        Api.getInstance().userDetail(id, nickname).subscribe(new HttpObserver<DetailResponse<User>>() {
            @Override
            public void onSucceeded(DetailResponse<User> data) {

                next(data.getData());
            }
        });
    }

    /**
     * 显示用户数据
     */
    private void next(User data) {

        this.data = data;
        initUI();

        ImageUtil.showAvatar(getMainActivity(),iv_avatar,data.getAvatar());
        tv_nickname.setText(data.getNickname());

        String info = getResources().getString(R.string.user_friend_info,data.getFollowings_count(),data.getFollowersCount());
        tv_info.setText(info);

        showFollowStatus();

    }

    /**
     * 显示关注按钮状态
     */
    private void showFollowStatus() {
        if (data.getId().equals(sp.getUserId())){
            btnLayout.setVisibility(View.GONE);
        }else {
            btnLayout.setVisibility(View.VISIBLE);
            bt_follow.setVisibility(View.VISIBLE);

            if (data.isFollowing()){
                bt_send_message.setVisibility(View.VISIBLE);
                bt_follow.setText(R.string.cancel_follow);

            }else {
                bt_send_message.setVisibility(View.GONE);
                bt_follow.setText(R.string.follow);
            }
        }
    }

    private void initUI() {

        adapter = new UserDetailAdapter(getMainActivity(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        List<Fragment> list = new ArrayList<>();
        list.add(UserDetailSheetFragment.getInstance(this.data.getId()));
        list.add(FeedFragment.newInstance(this.data.getId()));
        list.add(UserDetailAboutFragment.getInstance(this.data.getId()));

        adapter.setDatum(list);

        tab_layout.setupWithViewPager(viewPager);

    }

    /**
     * 关注和取消关注
     */
    @OnClick(R.id.bt_follow)
    public void onFollowClick(){

        if (data.isFollowing()){
            Api.getInstance().deleteFollow(data.getId()).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                @Override
                public void onSucceeded(DetailResponse<BaseModel> d) {
                    data.setFollowing(null);
                    showFollowStatus();
                }
            });
        }else {
            Api.getInstance().follow(data.getId()).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                @Override
                public void onSucceeded(DetailResponse<BaseModel> d) {
                    data.setFollowing(1);
                    showFollowStatus();
                }
            });

        }
    }
    @OnClick(R.id.bt_send_message)
    public void onSendMessageClick(){
        startActivity(ChatActivity.class);
    }

}
