package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.BaseRecyclerViewAdapter;
import com.example.mycloudmusic.adapter.FriendAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.UserResult;
import com.example.mycloudmusic.domain.event.SelectedFriendEvent;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.OnItemClickListener;
import com.example.mycloudmusic.util.DataUtil;
import com.example.mycloudmusic.util.PinyinUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class SelectFriendActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private FriendAdapter adapter;

    /**
     * 用户数据
     */
    private List<User> datum;

    /**
     * 用户数据处理结果
     */
    private UserResult userResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_friend);
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

        adapter = new FriendAdapter(getMainActivity());
        recycler_view.setAdapter(adapter);

        fetchData();
    }

    /**
     * 请求网络数据
     */
    private void fetchData() {
        Api.getInstance().friends(sp.getUserId()).subscribe(new HttpObserver<ListResponse<User>>() {
            @Override
            public void onSucceeded(ListResponse<User> data) {
                datum = data.getData();
                datum = DataUtil.processUserPinyin(datum);
                setData(datum);
            }
        });
    }

    /**
     * 设置数据
     *
     * @param datum
     */
    private void setData(List<User> datum) {
        //处理数据
        userResult = DataUtil.processUser(datum);

        //设置到适配器
        adapter.setDatum(userResult.getDatum());

//        //设置索引
//        ib.setTextArray(userResult.getLetters());
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                Object data = adapter.getData(position);
                if (data instanceof User) {
                    EventBus.getDefault().post(new SelectedFriendEvent((User) data));
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.hint_search_user));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("onQueryTextSubmit", query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange", newText);
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
