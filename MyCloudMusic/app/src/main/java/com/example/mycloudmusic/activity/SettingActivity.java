package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.fragment.ConfirmDialogFragment;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.st_play_mobile)
    Switch st_play_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        st_play_mobile.setChecked(sp.isMobilePlay());
    }

    @OnCheckedChanged(R.id.st_play_mobile)
    public void onPlayMobileCheckedChanged(CompoundButton view, boolean isChecked) {
        sp.setMobilePlay(isChecked);
    }

    @OnClick(R.id.ll_about)
    public void onAboutClick() {

        startActivity(AboutActivity.class);
    }

    @OnClick(R.id.bt_logout)
    public void onLogoutClick() {

        ConfirmDialogFragment.show(getSupportFragmentManager(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppContext.getInstance().logout();
            }
        });


    }
}
