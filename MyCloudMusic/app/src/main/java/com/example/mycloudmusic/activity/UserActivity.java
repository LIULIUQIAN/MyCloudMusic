package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.UserAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserActivity extends BaseTitleActivity {

    /**
     * 好友
     */
    public static final int FRIEND = 10;
    /**
     * 粉丝列表
     */
    public static final int FANS = 20;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    private UserAdapter adapter;
    private String userId;
    private int style;


    /**
     * 启动界面
     */
    public static void start(Activity activity, String userId, int style) {
        Intent intent = new Intent(activity, UserActivity.class);
        intent.putExtra(Constant.ID, userId);
        intent.putExtra(Constant.INT, style);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
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

        userId = extraId();
        style = getIntent().getIntExtra(Constant.INT, -1);

        adapter = new UserAdapter(R.layout.item_user);
        recycler_view.setAdapter(adapter);

        Observable<ListResponse<User>> api;
        if (style == FRIEND) {
            setTitle(R.string.my_friend);
            api = Api.getInstance().friends(userId);
        } else {
            setTitle(R.string.my_fans);
            api = Api.getInstance().fans(userId);
        }

        api.subscribe(new HttpObserver<ListResponse<User>>() {
            @Override
            public void onSucceeded(ListResponse<User> data) {
                adapter.replaceData(data.getData());
            }
        });

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
}
