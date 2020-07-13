package com.mgc.letobox.happy.util;

import android.app.Activity;
import android.support.annotation.Keep;

import com.mgc.leto.game.base.mgc.thirdparty.ILetoAuthListener;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

@Keep
public class WechatAuthUtil {
    public static void getWechatAuthInfo(Activity activity, ILetoAuthListener authListener) {
        UMAuthListener umAuthListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if(authListener!=null){
                    authListener.onStart(activity);
                }
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                if(authListener!=null){
                    authListener.onComplete(activity, i, map);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                if(authListener!=null){
                    authListener.onError(activity, i, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                if(authListener!=null){
                    authListener.onCancel(activity, i);
                }
            }
        };


        // get weixin info
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI umShareAPI = UMShareAPI.get(activity);
        umShareAPI.setShareConfig(config);
        umShareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }
}
