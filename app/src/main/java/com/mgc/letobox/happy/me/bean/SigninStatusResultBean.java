package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.ledong.lib.leto.mgc.bean.GetUserCoinResultBean;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-08-19
 **/
@Keep
public class SigninStatusResultBean {


    public List<SigninBean> getSignlist() {
        return signlist;
    }

    public void setSignlist(List<SigninBean> signlist) {
        this.signlist = signlist;
    }

    public GetUserCoinResultBean getCoins() {
        return coinslist;
    }

    public void setCoins(GetUserCoinResultBean coins) {
        this.coinslist = coins;
    }

    GetUserCoinResultBean coinslist;

    List<SigninBean> signlist;
}
