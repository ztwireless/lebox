package com.mgc.letobox.happy;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.ledong.lib.leto.Leto;
import com.mgc.leto.game.base.LetoCore;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.listener.ILetoInviteListener;
import com.mgc.leto.game.base.mgc.thirdparty.IAuthRequestListener;
import com.mgc.leto.game.base.mgc.thirdparty.ILetoAuthActivityListener;
import com.mgc.leto.game.base.mgc.thirdparty.ILetoAuthListener;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.letobox.happy.floattools.FloatToolsCenter;
import com.mgc.letobox.happy.follow.FollowInviteActivity;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.util.WechatAuthUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;


public class LetoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        LetoTrace.setDebugMode(true);

        //SDK 初始化 指定接入渠道
        Leto.init(this);

        LeBoxSpUtil.init(this);

        //UMeng
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, BaseAppUtil.getMetaStringValue(this, "UMENG_APPKEY"));
        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);

        //微信app配置
        if (BaseAppUtil.getMetaBooleanValue(this, "MGC_ENABLE_WECHAT_LOGIN")) {
            String wechatAppId = BaseAppUtil.getMetaStringValue(this, "MGC_WECHAT_APPID");
            String wechatAppSecret = BaseAppUtil.getMetaStringValue(this, "MGC_WECHAT_APPSECRET");
            if (!TextUtils.isEmpty(wechatAppId) && !TextUtils.isEmpty(wechatAppSecret)) {
                PlatformConfig.setWeixin(wechatAppId, wechatAppSecret);
            }
        }

        // 悬浮工具
        FloatToolsCenter.init(this);


        LetoEvents.setAuthActivityResultListener(new ILetoAuthActivityListener() {
            @Override
            public void onActivityForResult(Activity activity, int requestCode, int resultCode, Intent data) {
                WechatAuthUtil.setOnActivityResult(activity, requestCode, resultCode, data);
            }
        });


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
