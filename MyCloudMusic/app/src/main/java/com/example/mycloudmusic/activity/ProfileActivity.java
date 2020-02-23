package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Resource;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.event.OnUserChangedEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.fragment.DateDialogFragment;
import com.example.mycloudmusic.fragment.GenderDialogFragment;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.LoadingUtil;
import com.example.mycloudmusic.util.OSSUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.example.mycloudmusic.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mob.PrivacyPolicy;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileActivity extends BaseTitleActivity {

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.tv_gender)
    TextView tv_gender;

    @BindView(R.id.tv_birthday)
    TextView tv_birthday;

    @BindView(R.id.tv_area)
    TextView tv_area;

    @BindView(R.id.et_description)
    EditText et_description;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.tv_email)
    TextView tv_email;

    @BindView(R.id.bt_qq)
    Button bt_qq;

    @BindView(R.id.bt_weibo)
    Button bt_weibo;
    private User data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Api.getInstance().userDetail(sp.getUserId()).subscribe(new HttpObserver<DetailResponse<User>>() {
            @Override
            public void onSucceeded(DetailResponse<User> data) {

                next(data.getData());
            }
        });
    }

    /**
     * 显示用户信息
     */
    private void next(User data) {
        this.data = data;

        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getAvatar());

        et_nickname.setText(data.getNickname());
        tv_gender.setText(data.getGenderFormat());
        tv_birthday.setText(data.getBirthday());
        tv_area.setText(String.format("%s%s%s", data.getProvince(), data.getCity(), data.getArea()));
        et_description.setText(data.getDescription());
        tv_phone.setText(data.getPhone());
        tv_email.setText(data.getEmail());

        if (TextUtils.isEmpty(data.getQq_id())) {
            showUnbindButtonStatus(bt_qq);
        } else {
            showBindButtonStatus(bt_qq);
        }

        if (TextUtils.isEmpty(data.getWeibo_id())) {
            showUnbindButtonStatus(bt_weibo);
        } else {
            showBindButtonStatus(bt_weibo);
        }

    }

    private void showBindButtonStatus(Button button) {
        button.setText(R.string.bind);
        button.setTextColor(getResources().getColor(R.color.colorPrimary));
        button.setBackgroundResource(R.drawable.shape_border_color_primary);
    }

    private void showUnbindButtonStatus(Button button) {
        button.setText(R.string.unbind);
        button.setTextColor(getResources().getColor(R.color.light_grey));
        button.setBackgroundResource(R.drawable.shape_light_grey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_save) {
            onSaveClick();
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.avatar_container)
    public void onAvatarClick() {

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageSpanCount(4)
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @OnClick(R.id.gender_container)
    public void onGenderClick() {

        int index = 0;
        if (data.getGender() == User.MALE) {
            index = 1;
        } else if (data.getGender() == User.FEMALE) {
            index = 2;
        }

        GenderDialogFragment.show(getSupportFragmentManager(), index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 1) {
                    data.setGender(User.MALE);
                } else if (which == 2) {
                    data.setGender(User.FEMALE);
                } else {
                    data.setGender(User.GENDER_UNKNOWN);
                }
                next(data);
            }
        });
    }


    @OnClick(R.id.birthday_container)
    public void onBirthdayClick() {
        DateDialogFragment.show(getSupportFragmentManager(), new DateDialogFragment.DateListener() {
            @Override
            public void onSelected(String date) {
                data.setBirthday(date);
                next(data);
            }
        });
    }

    @OnClick(R.id.area_container)
    public void onAreaClick() {
        Log.e("onAvatarClick", "area_container");
    }

    @OnClick(R.id.bt_qq)
    public void onQQClick() {
        Log.e("onAvatarClick", "bt_qq");
    }

    @OnClick(R.id.bt_weibo)
    public void onWeiboClick() {
        Log.e("onAvatarClick", "bt_weibo");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> datum = PictureSelector.obtainMultipleResult(data);

            updateImage(datum.get(0).getCompressPath());
        }
    }

    /**
     * 头像上传
     */
    private void updateImage(String path) {

        new AsyncTask<String, Void, String>() {


            @Override
            protected String doInBackground(String... strings) {

                try {
                    OSSClient oss = OSSUtil.getInstance(getMainActivity());

                    String destFileName = UUIDUtil.getUUID() + ".jpg";
                    Log.e("doInBackground", "upload images:" + destFileName);
                    PutObjectRequest request = new PutObjectRequest(Constant.ALIYUN_OSS_BUCKET_NAME, destFileName, path);
                    //上传
                    oss.putObject(request);

                    return destFileName;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (TextUtils.isEmpty(s)) {
                    ToastUtil.errorShortToast(R.string.error_upload_image);
                } else {
                    data.setAvatar(s);
                    updateUserInfo();
                }
            }
        }.execute(path);
    }

    /**
     * 更新用户信息
     */
    private void updateUserInfo() {

        Api.getInstance().updateUser(data).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
            @Override
            public void onSucceeded(DetailResponse<BaseModel> data) {
                ToastUtil.successShortToast("用户信息更新成功");
                //发送通知
                EventBus.getDefault().post(new OnUserChangedEvent());

                finish();
            }
        });

    }

    /**
     * 保存按钮点击
     */
    private void onSaveClick() {

        String nickname = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.errorShortToast(R.string.enter_nickname);
            return;
        }

        data.setNickname(nickname);

        String description = et_description.getText().toString().trim();
        data.setDescription(description);

        updateUserInfo();
    }
}


