package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
@Keep
public class RewardButtonBean implements Serializable {

    public int type;  //类型

    public int resId; //icon 本地资源id
    public String name; //名称

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
