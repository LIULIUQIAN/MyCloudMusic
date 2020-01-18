package com.example.mycloudmusic.activity;


import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.mycloudmusic.R;

import butterknife.BindView;

public class BaseTitleActivity extends BaseCommonActivity {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void initViews() {
        super.initViews();

        setSupportActionBar(toolbar);

        //是否显示返回按钮
        if (isShowBackMenu()) {
            showBackMenu();
        }
    }

    private void showBackMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isShowBackMenu() {
        return true;
    }

    /**
     * 菜单点击了回调
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
