package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mycloudmusic.R;
import com.google.android.material.tabs.TabLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search){

        }
        return super.onOptionsItemSelected(item);
    }
}
