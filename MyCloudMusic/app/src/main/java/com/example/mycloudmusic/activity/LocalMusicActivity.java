package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.event.OnSelectSheetEvent;

import java.util.List;

public class LocalMusicActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        fetchData();
    }

    private void fetchData() {

        List<Song> songs = orm.queryLocalMusic(0);
        if (songs.size() > 0) {

        } else {

            startActivity(ScanLocalMusicActivity.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_music, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Log.e("onOptionsItemSelected", "action_edit");
                break;
            case R.id.action_scan_local_music:
                startActivity(ScanLocalMusicActivity.class);
                break;
            case R.id.action_sort:
                Log.e("onOptionsItemSelected", "action_sort");
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
