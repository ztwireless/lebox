package com.mgc.letobox.happy.follow.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Create by zhaozhihui on 2020-03-07
 **/

@Keep
public class FollowBindRequestBean extends BaseRequestBean {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile;
}
