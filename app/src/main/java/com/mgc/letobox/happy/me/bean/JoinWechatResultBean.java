package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.ledong.lib.leto.mgc.bean.GetUserCoinResultBean;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-08-19
 **/
@Keep
public class JoinWechatResultBean {

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    String qrcode;
    String explain;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    String pic;


}
