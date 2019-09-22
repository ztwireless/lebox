package com.mgc.letobox.happy;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ledong.lib.leto.utils.DeviceInfo;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;


/**
 * Created by zzh on 2018/3/14.
 */

public class CustomDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;



    public static Dialog showLogin(final Context context,final ConfirmDialogListener listener) {
        View dialogview = LayoutInflater.from(context).inflate(MResource.getIdByName(context,"R.layout.demo_dialog_login"), null);
        final Dialog dialog = new Dialog(context, MResource.getIdByName(context,"R.style.LetoCustomDialog"));
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(true);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        windowparams.width = DeviceInfo.getWidth(context);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        Button ok = (Button) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_btn_loginSubmit"));
        ImageView cancel = (ImageView) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_iv_close"));
        final EditText accountEt = (EditText) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_et_loginAccount"));
        final EditText uidEt = (EditText) dialogview.findViewById(MResource.getIdByName(context,"R.id.mgc_sdk_et_uid"));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    String uid = uidEt.getText().toString();
                    String mobile = accountEt.getText().toString();
                    if(!TextUtils.isEmpty(uid)) {
                        listener.login(uid, mobile);
                    }else{
                        ToastUtil.s(context, "请输入uid");
                    }
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.cancel();
                }
                dialog.dismiss();
            }
        });


        dialog.show();
        return dialog;
    }


    public interface ConfirmDialogListener {
        void login(String uid, String mobile);

        void cancel();
    }


}
