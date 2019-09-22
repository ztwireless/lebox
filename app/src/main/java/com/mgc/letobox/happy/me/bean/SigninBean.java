package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
@Keep
public class SigninBean implements Serializable {
    private int day;  // 签到第几天 从1 开始

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSign_coins() {
        return sign_coins;
    }

    public void setSign_coins(int sign_coins) {
        this.sign_coins = sign_coins;
    }

    public int getMultiple_reward() {
        return multiple_reward;
    }

    public void setMultiple_reward(int multiple_reward) {
        this.multiple_reward = multiple_reward;
    }

    public int getIs_get() {
        return is_get;
    }

    public void setIs_get(int is_get) {
        this.is_get = is_get;
    }

    int status;  //签到状态 0 不能签 到 1未签到 2签 到未领取 3 签到已 领取

    int sign_coins; //签到获得奖励

    int multiple_reward;  //看视频翻倍系数v

    int is_get;  //是否能直接领取 0 否1是

    public int getIs_today() {
        return is_today;
    }

    public void setIs_today(int is_today) {
        this.is_today = is_today;
    }

    int is_today;   //是否是今天 0 否 1 是

    public String getCoins_pic() {
        return coins_pic;
    }

    public void setCoins_pic(String coins_pic) {
        this.coins_pic = coins_pic;
    }

    String coins_pic;

    public SigninBean(int day, int sign_coins, int video_multiple, int status, int is_today, int is_get) {
        this.day = day;
        this.sign_coins = sign_coins;
        this.status = status;
        this.is_get = is_get;
        this.is_today = is_today;
        this.multiple_reward = video_multiple;
    }
}
