package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.event.OnFeedChangedEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.TextUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class PublishFeedActivity extends BaseTitleActivity {

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.tv_count)
    TextView tv_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
    }

    @Override
    protected void initListeners() {
        super.initListeners();


        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String info = getResources().getString(R.string.feed_count, s.toString().length());
                tv_count.setText(info);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.event_publish){
            onSendMessageClick();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布动态
     */
    private void onSendMessageClick() {

        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            ToastUtil.errorShortToast(R.string.hint_feed);
            return;
        }

        Feed feed = new Feed();
        feed.setContent(content);

        Api.getInstance().createFeed(feed).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
            @Override
            public void onSucceeded(DetailResponse<BaseModel> data) {

                EventBus.getDefault().post(new OnFeedChangedEvent());
                ToastUtil.successShortToast("动态发布成功");
                finish();
            }
        });

    }
}
