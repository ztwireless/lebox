package com.mgc.letobox.happy;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.listener.ILetoAdRewardListener;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.listener.ILetoSignInRewardListener;
import com.ledong.lib.leto.mgc.NewerTaskBean;
import com.ledong.lib.leto.mgc.thirdparty.IMintage;
import com.ledong.lib.leto.mgc.thirdparty.ISignin;
import com.ledong.lib.leto.mgc.thirdparty.MintageRequest;
import com.ledong.lib.leto.mgc.thirdparty.MintageResult;
import com.ledong.lib.leto.mgc.thirdparty.SigninRequest;
import com.ledong.lib.leto.mgc.thirdparty.SigninResult;
import com.leto.game.base.util.ToastUtil;
import com.tmsdk.TMSDKContext;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;

import java.util.List;


public class LetoApplication extends Application {

    public static int flag = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        //
        MultiDex.install(this);

        //SDK 初始化
        //Leto.init(this);

        System.out.println("Leto init before: " + System.currentTimeMillis());
        //SDK 初始化 指定接入渠道
        Leto.init(this);

        System.out.println("Leto init after: " + System.currentTimeMillis());

        //UMeng
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5b03dedda40fa32b7f00003c");
        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);

    }

}
