package com.mgc.letobox.happy;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

import com.ledong.lib.leto.Leto;
import com.mgc.leto.game.base.LetoCore;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.listener.ILetoInviteListener;
import com.mgc.leto.game.base.mgc.thirdparty.IAuthRequestListener;
import com.mgc.leto.game.base.mgc.thirdparty.ILetoAuthListener;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.letobox.happy.floattools.FloatToolsCenter;
import com.mgc.letobox.happy.follow.FollowInviteActivity;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.util.WechatAuthUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMShareAPI;


public class LetoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        LetoTrace.setDebugMode(true);

        // 使用竞价策略
        LetoCore.useBiddingAdPolicy(true);

        //无需sdk预加载广告sdk
        LetoCore.setAutoPreloadAd(false);

        //SDK 初始化 指定接入渠道
        Leto.init(this);

        LeBoxSpUtil.init(this);

        //UMeng
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, BaseAppUtil.getMetaStringValue(this, "UMENG_APPKEY"));
        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);

        // 悬浮工具
        FloatToolsCenter.init(this);


        LetoEvents.setAuthRequestListener(new IAuthRequestListener() {
            @Override
            public void requstAuth(Activity activity, ILetoAuthListener listener) {
                WechatAuthUtil.getWechatAuthInfo(activity, listener);
            }

        });

        LetoEvents.setInviteListener(new ILetoInviteListener() {
            @Override
            public void show(final Activity context, String gameId) {
                FollowInviteActivity.start(context);
            }

            @Override
            public void hide(Activity context, String s) {

            }
        });
    }
}
