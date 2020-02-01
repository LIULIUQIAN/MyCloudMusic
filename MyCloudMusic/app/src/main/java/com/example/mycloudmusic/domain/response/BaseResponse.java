package com.example.mycloudmusic.domain.response;

public class BaseResponse {

    /**
     * 状态码
     * 只有发生了错误才会有值
     */
    private int status;

    /**
     * 出错的提示信息
     */
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
