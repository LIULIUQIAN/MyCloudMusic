package com.example.mycloudmusic.domain;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class User extends BaseModel {

    /**
     * 未知
     */
    public static final int GENDER_UNKNOWN = 0;

    /**
     * 男
     */
    public static final int MALE = 10;

    /**
     * 女
     */
    public static final int FEMALE = 20;

    /**
     * 昵称
     */
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

    /**
     * 省
     */
    private String province;

    /**
     * 省编码
     */
    @SerializedName("province_code")
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市编码
     */
    @SerializedName("city_code")
    private String cityCode;


    /**
     * 区
     */
    private String area;

    /**
     * 区编码
     * <p>
     * SerializedName是GSON框架的功能
     * 所以如果使用其他JSON框架可能不支持
     * 更多的功能这里就不讲解了
     * <p>
     * 作用是指定序列化和反序列化时字段
     * 也就说说在JSON中该字段为area_code
     * 当然也可以不使用这个功能
     * 字段就定义为area_code
     * 只是在Java中推荐使用驼峰命名法
     */
    @SerializedName("area_code")
    private String areaCode;

    /**
     * 我的关注的人（好友）
     */
    private long followings_count;

    /**
     * 关注我的人（粉丝）
     */
    @SerializedName("followers_count")
    private long followersCount;

    /**
     * 是否关注
     * 1:关注
     * 在用户详情才会返回
     */
    private Integer following;

    /**
     * 性别
     * 0：保密，10：男，20：女
     * 可以定义为枚举
     * 但枚举性能没有int高
     * 但int没有一些编译验证
     * Android中有替代方式
     * 这里用不到就不讲解了
     */
    private int gender;

    /**
     * 生日
     * 格式为：yyyy-MM-dd
     */
    private String birthday;

    /**
     * 推送注册后的Id
     * 设备唯一
     * 作用是后台用来判断是否推送退出事件
     * 当然也可以通过其他方式实现
     */
    private String push;

    //本地过滤字段
    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 拼音首字母
     */
    private String pinyinFirst;

    /**
     * 拼音首字母的首字母
     */
    private String first;
    //end 本地过滤字段

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public long getFollowings_count() {
        return followings_count;
    }

    public void setFollowings_count(long followings_count) {
        this.followings_count = followings_count;
    }

    public long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(long followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }

    public void setPinyinFirst(String pinyinFirst) {
        this.pinyinFirst = pinyinFirst;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }


    ////
    /**
     * 格式化后的描述
     */
    public String getDescriptionFormat() {
        if (TextUtils.isEmpty(description)) {
            return "这个人很懒，没有填写个人介绍!";
        }

        return description;
    }

    public String getGenderFormat() {
        switch (gender) {
            case 10:
                return "男";
            case 20:
                return "女";
            default:
                //0
                return "保密";
        }
    }

    /**
     * 是否关注了
     */
    public boolean isFollowing() {
        return following != null;
    }


}
