package com.example.mycloudmusic.listener;

import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.domain.response.BaseResponse;
import com.example.mycloudmusic.util.HttpUtil;
import com.example.mycloudmusic.util.LoadingUtil;

import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public abstract class HttpObserver<T> extends ObserverAdapter<T> {

    private boolean isShowLoading;
    private BaseCommonActivity activity;

    public HttpObserver() {
    }

    public HttpObserver(BaseCommonActivity activity, boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
        this.activity = activity;
    }

    /*
     * 请求成功
     * */
    public abstract void onSucceeded(T data);

    /*
     * 请求失败
     * */
    public boolean onFailed(T data, Throwable e) {
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (isShowLoading) {
            LoadingUtil.showLoading(activity);
        }
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        LoadingUtil.hideLoading();

        if (isSucceeded(t)) {
            onSucceeded(t);
        } else {
            handlerRequest(t, null);
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        LoadingUtil.hideLoading();

        handlerRequest(null, e);

    }

    /*
     * 网络请求是否成功了
     * */
    private boolean isSucceeded(T t) {

        if (t instanceof Response) {
            Response response = (Response) t;
            int code = response.code();
            if (code >= 200 && code <= 299) {
                return true;
            }

        } else if (t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            return response.getStatus() == 0;
        }

        return false;
    }

    /*
     * 处理错误网络请求
     * */
    private void handlerRequest(T data, Throwable error) {

        if (onFailed(data, error)) {
            //返回true就表示外部手动处理错误
        } else {
            HttpUtil.handlerRequest(data, error);
        }

    }


}
