package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.SearchManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SearchHistoryAdapter;
import com.example.mycloudmusic.adapter.SearchResultAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.SearchHistory;
import com.example.mycloudmusic.domain.SearchTitle;
import com.example.mycloudmusic.domain.Suggest;
import com.example.mycloudmusic.domain.event.OnSearchEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.fragment.SheetFragment;
import com.example.mycloudmusic.fragment.UserFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.KeyboardUtil;
import com.example.mycloudmusic.util.LiteORMUtil;
import com.example.mycloudmusic.util.ViewUtil;
import com.google.android.material.tabs.TabLayout;
import com.nex3z.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchActivity extends BaseTitleActivity {

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.search_result_container)
    View search_result_container;

    @BindView(R.id.tl)
    TabLayout tl;

    @BindView(R.id.vp)
    ViewPager vp;

    /**
     * 搜索结果适配器
     */
    private SearchResultAdapter searchResultAdapter;
    private String data;
    private int selectedIndex;
    private SearchView searchView;
    private LiteORMUtil orm;
    private SearchHistoryAdapter searchHistoryAdapter;
    private FlowLayout fl;
    private SearchView.SearchAutoComplete searchAutoComplete;

    /**
     * 搜索建议适配器
     */
    private ArrayAdapter<String> suggestAdapter;
    private Runnable suggestionRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initViews() {
        super.initViews();
        ViewUtil.initVerticalLinearRecyclerView(getMainActivity(), rv);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        orm = LiteORMUtil.getInstance(getMainActivity());

        searchResultAdapter = new SearchResultAdapter(getMainActivity(), getSupportFragmentManager());
        vp.setAdapter(searchResultAdapter);

        List<Fragment> datum = new ArrayList<>();

        datum.add(SheetFragment.newInstance(0));
        datum.add(UserFragment.newInstance(1));
        searchResultAdapter.setDatum(datum);

        tl.setupWithViewPager(vp);

        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history);
        searchHistoryAdapter.addHeaderView(createHeaderView());
        rv.setAdapter(searchHistoryAdapter);

        fetchSearchHistoryData();
        fetchPopularData();
    }

    /**
     * 获取热门搜索数据
     */
    private void fetchPopularData() {

        List<String> datum = new ArrayList<>();

        datum.add("爱学啊");
        datum.add("我的云音乐");
        datum.add("人生苦短");
        datum.add("我们只做好课");
        datum.add("android开发");
        datum.add("项目课程");
        datum.add("Android项目实战");

        for (String data : datum) {
            View view = View.inflate(getMainActivity(), R.layout.item_tag, null);
            TextView tv = view.findViewById(R.id.tv_title);
            tv.setText(data);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSearchData(data);
                }
            });
            fl.addView(view);
        }

    }

    /**
     * 设置搜索数据并搜索
     */
    private void setSearchData(String data) {
        searchView.setQuery(data, true);

        searchView.setIconified(false);
        KeyboardUtil.hideKeyboard(getMainActivity());
    }

    /**
     * 创建header
     */
    private View createHeaderView() {

        View view = getLayoutInflater().inflate(R.layout.header_search, rv, false);
        fl = view.findViewById(R.id.fl);
        return view;
    }

    /**
     * 获取搜索历史
     */
    private void fetchSearchHistoryData() {
        List<SearchHistory> datum = orm.querySearchHistory();
        searchHistoryAdapter.replaceData(datum);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //保存索引
                selectedIndex = position;
                //执行搜索
                performSearch(data);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        searchHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter a, View view, int position) {
                orm.deleteSearchHistory(searchHistoryAdapter.getItem(position));
                searchHistoryAdapter.remove(position);
            }
        });
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                SearchHistory item = searchHistoryAdapter.getItem(position);
                setSearchData(item.getContent());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                prepareFetchSuggestion(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            showSearchHistoryView();
            return false;
        });

        //查找搜索建议控件
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        //默认要输入两个字符才显示提示，可以这样更改
        searchAutoComplete.setThreshold(1);
        //获取搜索管理器
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        //设置搜索信息
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //设置搜索建议点击回调
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> setSearchData(suggestAdapter.getItem(position)));

        return true;
    }

    /**
     * 准备获取搜索建议
     */
    private void prepareFetchSuggestion(String data) {

        if (TextUtils.isEmpty(data)) {
            return;
        }
        if (suggestionRunnable != null) {
            vp.removeCallbacks(suggestionRunnable);
            suggestionRunnable = null;

        }

        suggestionRunnable = new Runnable() {
            @Override
            public void run() {
                Api.getInstance()
                        .searchSuggest(data)
                        .subscribe(new HttpObserver<DetailResponse<Suggest>>() {
                            @Override
                            public void onSucceeded(DetailResponse<Suggest> data) {
                                setSuggest(data.getData());
                            }
                        });
            }
        };

        vp.postDelayed(suggestionRunnable, 1000);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (searchView.isIconified()) {
            super.onBackPressed();
        } else {
            searchView.setIconified(true);
        }

    }

    /**
     * 执行搜索
     */
    private void performSearch(String data) {
        this.data = data;

        if (TextUtils.isEmpty(data)) {
            return;
        }

        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setContent(data);
        searchHistory.setCreated_at(System.currentTimeMillis());

        orm.createOrUpdate(searchHistory);

        fetchSearchHistoryData();

        showSearchResultView();

        EventBus.getDefault().post(new OnSearchEvent(data, selectedIndex));
    }

    /**
     * 显示搜索结果控件
     */
    private void showSearchResultView() {
        search_result_container.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }

    /**
     * 显示搜索历史控件
     */
    private void showSearchHistoryView() {
        search_result_container.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置搜索建议
     */
    private void setSuggest(Suggest data) {

        //处理搜索建议数据

        //像变换这个中操作
        //如果是Kotlin语言中就一句话的事

        List<String> datum = new ArrayList<>();

        //处理歌单搜索建议
        if (data.getSheets() != null) {
            for (SearchTitle title : data.getSheets()
            ) {
                datum.add(title.getTitle());
            }
        }

        //处理用户搜索建议
        if (data.getUsers() != null) {
            for (SearchTitle title : data.getUsers()
            ) {
                datum.add(title.getTitle());
            }
        }

        //创建适配器
        suggestAdapter = new ArrayAdapter<>(getMainActivity(),
                R.layout.item_suggest,
                R.id.tv_title,
                datum);

        //设置到控件
        searchAutoComplete.setAdapter(suggestAdapter);
    }


}
