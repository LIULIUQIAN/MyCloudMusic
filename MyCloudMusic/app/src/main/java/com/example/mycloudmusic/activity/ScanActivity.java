package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.ToastUtil;
import com.example.mycloudmusic.util.UrlUtil;
import com.king.zxing.CaptureHelper;
import com.king.zxing.OnCaptureCallback;
import com.king.zxing.ViewfinderView;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanActivity extends BaseTitleActivity implements OnCaptureCallback {

    /**
     * 扫描预览视图
     */
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;

    /**
     * 扫描框
     */
    @BindView(R.id.viewfinderView)
    ViewfinderView viewfinderView;

    /**
     * 扫描工具类
     */
    private CaptureHelper captureHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void initViews() {
        super.initViews();

        //显示亮色状态
        lightStatusBar();
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        //创建扫描工具类
        captureHelper = new CaptureHelper(this, surfaceView, viewfinderView);

        //设置扫描结果回调
        captureHelper.setOnCaptureCallback(this);

        //设置支持连续扫描
        captureHelper.continuousScan(true);

        //执行创建
        captureHelper.onCreate();
    }

    /**
     * 界面显示了
     */
    @Override
    protected void onResume() {
        super.onResume();

        //开始扫描
        captureHelper.onResume();
    }

    /**
     * 界面暂停了
     */
    @Override
    protected void onPause() {
        super.onPause();

        //暂停扫描
        captureHelper.onPause();
    }

    /**
     * 界面销毁时
     */
    @Override
    protected void onDestroy() {
        //销毁扫描工具类
        captureHelper.onDestroy();

        super.onDestroy();
    }

    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件发生到扫描工具类
        captureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 我的二维码点击
     */
    @OnClick(R.id.bt_code)
    public void onCodeClick() {
        startActivityExtraId(CodeActivity.class, sp.getUserId());
    }

    /**
     * 扫描结果回调
     */
    @Override
    public boolean onResultCallback(String result) {

        if (StringUtils.isNotBlank(result)) {
            //处理扫描结果
            handleScanString(result);
        } else {
            //显示不支持该格式
            showNotSupportFormat();
        }

        //拦截结果
        return true;
    }

    /**
     * 显示不支持该格式
     */
    private void showNotSupportFormat() {
        //先暂停
        captureHelper.onPause();

        ToastUtil.errorShortToast(R.string.error_not_support_qrcode_format);

        //延迟后启用扫描
        viewfinderView.postDelayed(() -> captureHelper.onResume(), 1000);
    }

    /**
     * 处理扫描结果
     */
    private void handleScanString(String data) {

        //解析出网址中的查询参数
        Map query = UrlUtil.getUrlQuery(data);

        //获取用户id值
        String userId = (String) query.get("u");
        if (StringUtils.isNotBlank(userId)) {
            //有值
            processUserCode(userId);
        } else {
            //显示不支持该类型
            showNotSupportFormat();
        }
    }

    /**
     * 处理用户二维码
     */
    private void processUserCode(String userId) {
        //关闭当前界面
        finish();

        //跳转到用户详情
        UserDetailActivity.start(getMainActivity(), userId,null);
    }

}
