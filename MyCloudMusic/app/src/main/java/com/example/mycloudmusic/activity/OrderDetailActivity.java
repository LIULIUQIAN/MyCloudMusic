package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Order;
import com.example.mycloudmusic.domain.event.OnPaySuccessEvent;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseTitleActivity {

    @BindView(R.id.tv_status)
    TextView tv_status;

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_created_at)
    TextView tv_created_at;

    @BindView(R.id.tv_source)
    TextView tv_source;

    @BindView(R.id.tv_origin)
    TextView tv_origin;

    @BindView(R.id.tv_channel)
    TextView tv_channel;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.pay_container)
    View pay_container;

    @BindView(R.id.control_container)
    View control_container;

    private Order data;

    /**
     * 是否通知支付状态
     */
    private boolean isNotifyPayStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        Api.getInstance().orderDetail(extraId()).subscribe(new HttpObserver<DetailResponse<Order>>() {
            @Override
            public void onSucceeded(DetailResponse<Order> data) {
                next(data.getData());
            }
        });
    }

    private void next(Order order){

        this.data = order;

        tv_status.setText(getResources().getString(R.string.order_status_value,order.getStatusFormat()));
        tv_number.setText(getResources().getString(R.string.order_number_value,order.getNumber()));

        ImageUtil.showAvatar(getMainActivity(),iv_banner,order.getBook().getBanner());

        tv_title.setText(order.getBook().getTitle());

        String created = TimeUtil.yyyyMMddHHmmss(order.getCreated_at());
        tv_created_at.setText(getResources().getString(R.string.order_created_value, created));

        tv_source.setText(getResources().getString(R.string.order_source_value,order.getSourceFormat()));

        //订单来源
        tv_source.setText(getResources().getString(R.string.order_source_value, order.getSourceFormat()));

        //价格
        tv_price.setText(getResources().getString(R.string.price, order.getPrice()));

    }

    /**
     * 显示订单状态
     */
    private void showOrderStatus() {
        //订单状态
        tv_status.setText(getResources().getString(R.string.order_status_value, data.getStatusFormat()));

        //订单状态颜色
        tv_status.setTextColor(getResources().getColor(data.getStatusColor()));

        //支付平台
        tv_origin.setText(getResources().getString(R.string.origin_source_value, data.getOriginFormat()));

        //支付渠道
        tv_channel.setText(getResources().getString(R.string.order_channel_value, data.getChannelFormat()));

        switch (data.getStatus()) {
            case Order.PAYED:
            case Order.CLOSE:
                //支付成功

                //隐藏支付渠道
                pay_container.setVisibility(View.GONE);

                //隐藏控制容器
                control_container.setVisibility(View.GONE);

                if (data.getStatus() == Order.PAYED && isNotifyPayStatus) {
                    //发送支付成功通知
                    EventBus.getDefault().post(new OnPaySuccessEvent());

                    isNotifyPayStatus = false;
                }

                break;
            default:
                //显示支付渠道
                pay_container.setVisibility(View.VISIBLE);

                //显示控制容器
                control_container.setVisibility(View.VISIBLE);
                break;
        }

    }

    @OnClick(R.id.bt_control)
    public void onControlClick(){

    }
}
