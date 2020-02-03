package com.example.mycloudmusic.domain;

import android.text.TextUtils;

public class User extends BaseModel {

    /*
     * 昵称
     * */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户的密码,登录，注册向服务端传递
     */
    private String password;

    /**
     * QQ第三方登录后Id
     */
    private String qq_id;

    /**
     * 微博第三方登录后Id
     */
    private String weibo_id;

    /**
     * 验证码
     * 只有找回密码的时候才会用到
     */
    private String code;

    /**
     * 描述
     */
    private String description;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQq_id() {
        return qq_id;
    }

    public void setQq_id(String qq_id) {
        this.qq_id = qq_id;
    }

    public String getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id) {
        this.weibo_id = weibo_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ////
    /**
     * 格式化后的描述
     *
     * @return
     */
    public String getDescriptionFormat() {
        if (TextUtils.isEmpty(description)) {
            return "这个人很懒，没有填写个人介绍!";
        }

        return description;
    }

}
