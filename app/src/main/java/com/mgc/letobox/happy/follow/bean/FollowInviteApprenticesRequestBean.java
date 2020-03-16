package com.mgc.letobox.happy.follow.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BasePageRequetBean;

/**
 * Create by zhaozhihui on 2020-03-07
 **/

@Keep
public class FollowInviteApprenticesRequestBean extends BasePageRequetBean {

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    String mobile;
}
