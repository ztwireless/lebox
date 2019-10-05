package com.mgc.letobox.happy.find.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.leto.game.base.util.BaseAppUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.bean.ShareBean;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zzh on 2018/3/14.
 */

/**
 * Created by zzh on 2018/3/14.
 */
public class SharePlatformDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    private Context mContext;

    public void showDialog(Context context, ShareBean share, ConfirmDialogListener listener) {
        dismiss();
        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_share_platform, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
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

        LinearLayout ll_close = (LinearLayout) dialogview.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.cancel();
                }
                dismiss();
            }
        });

        LinearLayout ll_wechat = (LinearLayout) dialogview.findViewById(R.id.ll_wechat);
        LinearLayout ll_wechatmoments = (LinearLayout) dialogview.findViewById(R.id.ll_wechatmoments);
        ll_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_MEDIA.WEIXIN);
                }
                dismiss();
            }
        });
        ll_wechatmoments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                dismiss();
            }
        });

        LinearLayout ll_qq = (LinearLayout) dialogview.findViewById(R.id.ll_qq);
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistener != null) {
                    mlistener.setPlatform(SHARE_MEDIA.QQ);
                }
                dismiss();
            }
        });

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            mlistener = null;
        }
    }

    public interface ConfirmDialogListener {
        void setPlatform(SHARE_MEDIA platform);

        void cancel();
    }




}
