package com.mgc.letobox.happy.me;

import android.content.Context;

/**
 * Create by zhaozhihui on 2020/12/10
 **/
public interface IRewardAdResult {
    public void onSuccess();
    public void onFail(String code, String message);
}
