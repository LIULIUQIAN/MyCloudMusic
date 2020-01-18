package com.example.mycloudmusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.GuideAdapter;
import com.example.mycloudmusic.fragment.GuideFragment;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GuideActivity extends BaseCommonActivity implements View.OnClickListener {

    private Button bt_login_or_register;
    private Button bt_enter;
    private GuideAdapter adapter;
    private ViewPager vp;
    private CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

    }

    @Override
    protected void initViews() {
        super.initViews();
        //隐藏状态栏
        hideStatusBar();
        vp = findViewById(R.id.vp);
        indicator = findViewById(R.id.indicator);
        bt_login_or_register = findViewById(R.id.bt_login_or_register);
        bt_enter = findViewById(R.id.bt_enter);

    }

    @Override
    protected void initDatum() {
        super.initDatum();
        adapter = new GuideAdapter(getMainActivity(),getSupportFragmentManager());
        vp.setAdapter(adapter);

        indicator.setViewPager(vp);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        List<Integer> datas = new ArrayList<>();
        datas.add(R.drawable.guide1);
        datas.add(R.drawable.guide2);
        datas.add(R.drawable.guide3);
        datas.add(R.drawable.guide4);
        datas.add(R.drawable.guide5);
        adapter.setDatum(datas);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        bt_login_or_register.setOnClickListener(this);
        bt_enter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_or_register:
                startActivityAfterFinishThis(LoginOrRegisterActivity.class);
                sp.setShowGuide(false);
                break;
            case R.id.bt_enter:
                startActivityAfterFinishThis(MainActivity.class);
                sp.setShowGuide(false);
                break;
        }
    }
}
