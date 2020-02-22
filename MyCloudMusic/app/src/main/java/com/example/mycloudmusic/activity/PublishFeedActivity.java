package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.ImageSelectAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.Resource;
import com.example.mycloudmusic.domain.event.OnFeedChangedEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.LoadingUtil;
import com.example.mycloudmusic.util.OSSUtil;
import com.example.mycloudmusic.util.TextUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.example.mycloudmusic.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PublishFeedActivity extends BaseTitleActivity {

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.tv_count)
    TextView tv_count;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageSelectAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_feed);
    }

    @Override
    protected void initViews() {
        super.initViews();
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new ImageSelectAdapter(R.layout.item_image_select);
        recyclerView.setAdapter(adapter);

        setData(new ArrayList<>());

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

        adapter.setOnItemClickListener((adapter, view, position) -> {

            if (adapter.getItem(position) instanceof Integer) {
                selectImage();
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            adapter.remove(position);

            if (adapter.getItem(adapter.getData().size() - 1) instanceof LocalMedia) {
                adapter.addData(R.drawable.ic_add_grey);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.event_publish) {
            onSendMessageClick();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 发布动态
     */
    private void onSendMessageClick() {

        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.errorShortToast(R.string.hint_feed);
            return;
        }

        //获取选中的图片
        List<LocalMedia> selectedImages = getSelectedImages();
        if (selectedImages.size() > 0) {
            uploadImages(selectedImages);
        } else {
            saveFeed(null);
        }


    }


    /**
     * 获取要上传的图片
     */
    private List<LocalMedia> getSelectedImages() {

        List<Object> data = adapter.getData();

        List<LocalMedia> datum = new ArrayList<>();
        for (Object o : data) {
            if (o instanceof LocalMedia) {
                datum.add((LocalMedia) o);
            }
        }
        return datum;
    }

    /**
     * 上传图片
     */
    private void uploadImages(List<LocalMedia> datum) {

        new AsyncTask<List<LocalMedia>, Integer, List<Resource>>() {

            /**
             * 异步任务执行前调用
             * 主线程中调用
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                LoadingUtil.showLoading(getMainActivity(), "图片上传中...");
            }

            /**
             * 子线程中执行
             */
            @Override
            protected List<Resource> doInBackground(List<LocalMedia>... params) {

                OSSClient oss = OSSUtil.getInstance(getMainActivity());

                ArrayList<Resource> results = new ArrayList<>();

                List<LocalMedia> images = params[0];
                try {

                    for (int i = 0; i < images.size(); i++) {

                        LocalMedia localMedia = images.get(i);
                        publishProgress(i);

                        String destFileName = UUIDUtil.getUUID() + ".jpg";
                        Log.e("doInBackground", "upload images:" + destFileName);
                        PutObjectRequest request = new PutObjectRequest(Constant.ALIYUN_OSS_BUCKET_NAME, destFileName, localMedia.getCompressPath());
                        PutObjectResult putObjectResult = oss.putObject(request);

                        results.add(new Resource(destFileName));
                    }
                    return results;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            /**
             * 进度回调
             * 主线程中执行
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                Integer index = values[0] + 1;
                LoadingUtil.showLoading(getMainActivity(), "正在上传第" + index + "张图片");
            }

            /**
             * 异步任务执行完成了
             */
            @Override
            protected void onPostExecute(List<Resource> resources) {
                super.onPostExecute(resources);
                LoadingUtil.hideLoading();

                if (datum != null && resources.size() == datum.size()) {
                    saveFeed(resources);
                } else {
                    ToastUtil.errorShortToast(R.string.error_upload_image);
                }
            }
        }.execute(datum);
    }

    /**
     * 发布动态
     */
    private void saveFeed(List<Resource> images) {

        String content = et_content.getText().toString().trim();

        Feed feed = new Feed();
        feed.setContent(content);
        feed.setImages(images);

        Api.getInstance().createFeed(feed).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
            @Override
            public void onSucceeded(DetailResponse<BaseModel> data) {

                EventBus.getDefault().post(new OnFeedChangedEvent());
                ToastUtil.successShortToast("动态发布成功");
                finish();
            }
        });
    }

    /**
     * 选择图片
     */
    @OnClick(R.id.ib_select_image)
    public void onSelectImageClick() {
        selectImage();
    }

    /**
     * 选择图片
     */
    private void selectImage() {
        //进入相册
        //以下是例子
        //用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                //.enableCrop()// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> datum = PictureSelector.obtainMultipleResult(data);

                setData(Lists.newArrayList(datum));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 设置数据
     *
     * @param datum
     */
    private void setData(List<Object> datum) {
        if (datum.size() != 9) {
            datum.add(R.drawable.ic_add_grey);
        }
        adapter.replaceData(datum);
    }
}
