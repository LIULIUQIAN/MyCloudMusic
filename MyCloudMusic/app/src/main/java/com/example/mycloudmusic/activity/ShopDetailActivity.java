package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Book;
import com.example.mycloudmusic.domain.param.OrderParam;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ShopDetailActivity extends BaseTitleActivity {

    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.bt_control)
    Button bt_control;


    private String shopId;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        shopId = extraId();

        Api.getInstance().shopDetail(shopId).subscribe(new HttpObserver<DetailResponse<Book>>() {
            @Override
            public void onSucceeded(DetailResponse<Book> data) {

                next(data.getData());
            }
        });

    }

    private void next(Book data) {
        book = data;

        ImageUtil.showAvatar(getMainActivity(), iv_banner, book.getBanner());
        tv_title.setText(book.getTitle());
        String price = getResources().getString(R.string.price, book.getPrice());
        tv_price.setText(price);

        if (book.isBuy()) {
            bt_control.setText(R.string.go_study);
            bt_control.setBackgroundColor(getResources().getColor(R.color.color_pass));
        }
    }

    @OnClick(R.id.bt_control)
    public void onControlClick() {

        if (book.isBuy()) {
            ToastUtil.successShortToast(R.string.success_buy);
            return;
        }
        //创建订单
        OrderParam param = new OrderParam();
        param.setBook_id(book.getId());

        Api.getInstance().createOrder(param).subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
            @Override
            public void onSucceeded(DetailResponse<BaseModel> data) {

                startActivityExtraId(OrderDetailActivity.class, data.getData().getId());
            }
        });


    }

}
