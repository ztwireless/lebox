package com.mgc.letobox.happy.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/12.
 */
@Keep
public class VersionRequestBean extends BaseRequestBean {
    private String  version;   //钱包版本号

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

    private int type;   //钱包类型 1安卓 2ios
}
