package com.mgc.letobox.happy.follow;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mgc.leto.game.base.bean.SHARE_PLATFORM;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.MResource;

/**
 * Create by zhaozhihui on 2020-03-05
 **/
public class SharePlatformDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    private Context mContext;

    public Dialog showDialog(Context context, ConfirmDialogListener listener) {
        dismiss();
        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(MResource.getIdByName(context,"R.layout.follow_dialog_share_platform" ), null);
        dialog = new Dialog(context,MResource.getIdByName(context, "R.style.leto_custom_dialog") );
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        windowparams.width = BaseAppUtil.getDeviceWidth(context);
//        设置背景透明,但是那个标题头还是在的，只是看不见了

        LinearLayout ll_close = (LinearLayout) dialogview.findViewById(MResource.getIdByName(context,"R.id.ll_close"));
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.cancel();
                }
                dismiss();
            }
        });

        LinearLayout ll_wechat = (LinearLayout) dialogview.findViewById(MResource.getIdByName(context,"R.id.ll_wechat"));
        LinearLayout ll_wechatmoments = (LinearLayout) dialogview.findViewById(MResource.getIdByName(context,"R.id.ll_wechatmoments"));
        ll_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_PLATFORM.WEIXIN);
                }
                dismiss();
            }
        });
        ll_wechatmoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_PLATFORM.WEIXIN_CIRCLE);
                }
                dismiss();
            }
        });

        LinearLayout ll_facetoface = (LinearLayout) dialogview.findViewById(MResource.getIdByName(context,"R.id.ll_facetoface"));
        ll_facetoface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_PLATFORM.FACE_TO_FACE);
                }
                dismiss();
            }
        });



        dialog.show();
        return  dialog;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            mlistener = null;
        }
    }

}
