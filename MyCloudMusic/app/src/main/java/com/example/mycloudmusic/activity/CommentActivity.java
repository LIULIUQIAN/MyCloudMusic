package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.github.jdsjlzx.recyclerview.LRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.mycloudmusic.util.Constant.SHEET_ID;

public class CommentActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    LRecyclerView recycler_view;

    @BindView(R.id.et_content)
    EditText et_content;

    /**
     * 启动评论界面
     */
    public static void start(Activity activity, String sheetId) {

        Intent intent = new Intent(activity, CommentActivity.class);
        //歌单id
        intent.putExtra(SHEET_ID, sheetId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Map<String, String> map = new HashMap<>();
        Api.getInstance().comments(map).subscribe(new HttpObserver<ListResponse<Comment>>() {
            @Override
            public void onSucceeded(ListResponse<Comment> data) {

                Log.e("CommentActivity", data.getData().size() + "");
            }
        });
    }

    @OnClick(R.id.bt_send)
    public void onSendClick(){
        Log.e("aa","aaa");
    }
}
