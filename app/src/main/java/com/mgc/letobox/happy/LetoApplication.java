package com.mgc.letobox.happy;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.ledong.lib.leto.Leto;
import com.leto.game.base.util.BaseAppUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;


public class LetoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        //SDK 初始化 指定接入渠道
        Leto.init(this);

        //UMeng
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, BaseAppUtil.getMetaStringValue(this, "UMENG_APPKEY"));
        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);
    }
}
