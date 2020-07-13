package com.mgc.letobox.happy.bean;

import android.support.annotation.Keep;

import com.mgc.leto.game.base.bean.BaseRequestBean;

@Keep
public class VersionRequestBean extends BaseRequestBean {
    private String  version;   //钱包版本号
    private int type;   //钱包类型 1安卓 2ios

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
