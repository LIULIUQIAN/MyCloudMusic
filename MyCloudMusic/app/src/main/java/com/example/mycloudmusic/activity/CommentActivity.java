package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.BaseRecyclerViewAdapter;
import com.example.mycloudmusic.adapter.CommentAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.event.SelectedFriendEvent;
import com.example.mycloudmusic.domain.event.SelectedTopicEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.fragment.CommentMoreDialogFragment;
import com.example.mycloudmusic.listener.CommentAdapterListener;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.OnItemClickListener;
import com.example.mycloudmusic.util.ClipboardUtil;
import com.example.mycloudmusic.util.KeyboardUtil;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

import static com.example.mycloudmusic.util.Constant.HAST_TAG;
import static com.example.mycloudmusic.util.Constant.MENTION;
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
     * 输入框上一次长度
     */
    private int lastContentLength;

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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

        EventBus.getDefault().register(this);
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

                showCommentMoreDialog(adapter.getData(position));
            }
        });

        adapter.setCommentAdapterListener(new CommentAdapterListener() {
            @Override
            public void onAvatarClick(Comment data) {
                UserDetailActivity.start(getMainActivity(), data.getUser().getId(), null);
            }

            @Override
            public void onLikeClick(Comment data) {

                if (data.isLiked()) {
                    Api.getInstance().deleteLike(data.getLike_id()).subscribe(new HttpObserver<Response<Void>>() {
                        @Override
                        public void onSucceeded(Response<Void> d) {
                            data.setLikes_count(data.getLikes_count() - 1);
                            data.setLike_id(null);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    Api.getInstance().like(data.getId()).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                        @Override
                        public void onSucceeded(DetailResponse<BaseModel> d) {
                            data.setLikes_count(data.getLikes_count() + 1);
                            data.setLike_id(d.getData().getId());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (Math.abs(dy) > 30) {
                    if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                        clearInputContent();
                    }
                }
            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int currentLength = s.toString().length();

                if (currentLength > lastContentLength) {
                    String data = s.toString();
                    if (data.endsWith(HAST_TAG)) {
                        startActivity(SelectTopicActivity.class);
                    } else if (data.endsWith(MENTION)) {
                        startActivity(SelectFriendActivity.class);
                    }
                }
                lastContentLength = currentLength;

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
        //设置被回复评论的id
        data.setParent_id(parentId);
        //设置内容
        data.setContent(content);
        //设置歌单id
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
                        parentId = data.getId();
                        et_content.setHint(getResources().getString(R.string.reply_hint, data.getUser().getNickname()));
                        break;
                    case 1:
                        ClipboardUtil.copyText(getMainActivity(), data.getContent());
                        ToastUtil.successShortToast(R.string.copy_success);
                        break;
                }
            }
        });
    }

    /*
     * 话题选择回调
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectTopicClickEvent(SelectedTopicEvent event) {

        et_content.append(event.getData().getTitle());
        et_content.append("# ");

        highlightText();
    }
    /**
     * 用户选择回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectFriendClickEvent(SelectedFriendEvent event){
        et_content.append(event.getData().getNickname());
        et_content.append(" ");

        highlightText();
    }

    /**
     * 文本高亮
     */
    private void highlightText() {
        SpannableString spannableString = StringUtil.processHighlight(getMainActivity(), et_content.getText().toString());
        et_content.setText(spannableString);
        et_content.setSelection(et_content.getText().toString().length());
    }


}
