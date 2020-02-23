package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.DensityUtil;
import com.example.mycloudmusic.util.ImageUtil;
import com.king.zxing.util.CodeUtils;

import butterknife.BindView;

import static com.example.mycloudmusic.util.Constant.QRCODE_URL;

public class CodeActivity extends BaseTitleActivity {

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.iv_code)
    ImageView iv_code;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
    }

    @Override
    protected void initViews() {
        super.initViews();

        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        userid = extraId();

        Api.getInstance().userDetail(userid).subscribe(new HttpObserver<DetailResponse<User>>() {
            @Override
            public void onSucceeded(DetailResponse<User> data) {
                next(data.getData());
            }
        });
    }

    private void next(User data){
        ImageUtil.showAvatar(getMainActivity(),iv_avatar,data.getAvatar());
        tv_nickname.setText(data.getNickname());

        String qrCodeData = QRCODE_URL + data.getId();

        Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        int wh = DensityUtil.dipTopx(getMainActivity(), 250);
        Bitmap qrCodeBitmap = CodeUtils.createQRCode(qrCodeData, wh, logo);
        iv_code.setImageBitmap(qrCodeBitmap);

    }
}
