package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Create by zhaozhihui on 2019-09-14
 **/

@Keep
public class ExchangeRequestBean extends BaseRequestBean {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;
}
