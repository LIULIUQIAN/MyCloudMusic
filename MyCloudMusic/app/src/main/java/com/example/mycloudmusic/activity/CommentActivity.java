package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.BaseRecyclerViewAdapter;
import com.example.mycloudmusic.adapter.CommentAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.fragment.CommentMoreDialogFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.OnItemClickListener;
import com.example.mycloudmusic.util.KeyboardUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

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

    private LRecyclerViewAdapter adapterWrapper;
    private CommentAdapter adapter;

    /**
     * 歌单id
     */
    private String sheetId;

    /**
     * 被回复评论的id
     */
    private String parentId;

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
    protected void initViews() {
        super.initViews();

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        DividerItemDecoration decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        recycler_view.addItemDecoration(decoration);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        sheetId = extraString(SHEET_ID);

        adapter = new CommentAdapter(getMainActivity());
        adapterWrapper = new LRecyclerViewAdapter(adapter);

        recycler_view.setAdapter(adapterWrapper);

        fetchData();

    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
                Log.e("OnItemClickListener", "===========" + position);

                parentId = adapter.getData(position).getParent_id();
                showCommentMoreDialog(adapter.getData(position));
            }
        });
    }

    /*
     * 请求网络数据
     * */
    private void fetchData() {

        Map<String, String> map = new HashMap<>();
        Api.getInstance().comments(map).subscribe(new HttpObserver<ListResponse<Comment>>() {
            @Override
            public void onSucceeded(ListResponse<Comment> data) {

                adapter.setDatum(data.getData());

            }
        });

    }

    @OnClick(R.id.bt_send)
    public void onSendClick() {

        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.errorShortToast(R.string.enter_comment);
            return;
        }

        Comment data = new Comment();
        data.setParent_id(parentId);

        data.setContent(content);
        data.setSheet_id(sheetId);
        Api.getInstance().createComment(data).subscribe(new HttpObserver<DetailResponse<Comment>>() {
            @Override
            public void onSucceeded(DetailResponse<Comment> data) {
                ToastUtil.successShortToast(R.string.comment_create_success);
                fetchData();
                clearInputContent();

                //关闭键盘
                KeyboardUtil.hideKeyboard(getMainActivity());
            }
        });
    }

    /**
     * 清空输入框
     */
    private void clearInputContent() {
        parentId = null;
        et_content.setText("");
        et_content.setHint(R.string.enter_comment);
    }

    /**
     * 显示评论更多对话框
     *
     * @param data
     */
    private void showCommentMoreDialog(Comment data) {

        CommentMoreDialogFragment fragment = CommentMoreDialogFragment.getInstance();
        fragment.show(getSupportFragmentManager(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which) {
                    case 0:
                        Log.e("", "回复评论" + data.getContent());
                        break;
                    case 1:
                        Log.e("", "复制评论" + data.getContent());
                        break;
                }
            }
        });
    }
}
