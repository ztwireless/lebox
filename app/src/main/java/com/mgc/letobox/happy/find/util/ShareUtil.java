package com.mgc.letobox.happy.find.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mgc.letobox.happy.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by zzh on 2018/3/15.
 */

public class ShareUtil {
    public static void shareToPlatform(Context context, SHARE_MEDIA platform, UMShareListener shareListener, String url, String title, String content, String image){
        UMImage thumb =null;

        if(TextUtils.isEmpty(image)){
            thumb = new UMImage(context, R.mipmap.ic_launcher);
        }else{
            thumb = new UMImage(context, image);
        }

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(content);//描述

        new ShareAction((Activity) context)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(shareListener)//回调监听器
                .share();
    }
}
