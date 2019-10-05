package com.mgc.letobox.happy.circle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.mgc.letobox.happy.R;


public class CommonDialog {
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvTitle;
    TextView tvContent;

    TextView btok;
    TextView btcancel;

    public void showDialog(Context context, boolean showCancel, String title, String content, ConfirmDialogListener listener){
        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
////        window.setGravity(Gravity.BOTTOM);
//        windowparams.height = ;
        windowparams.width = BaseAppUtil.getDeviceWidth(context)-2* DensityUtil.dip2px(context, 16);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        if(title!=null){
            tvTitle.setText(title);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.cancel();
                }
                dismiss();
            }
        }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void setContentLeftAlign(){
        if(tvContent!=null){
            tvContent.setGravity(Gravity.START);
        }
    }

    public void showDialog(Context context, boolean showCancel, String title, String content,String cancelText, String confirmText, ConfirmDialogListener listener, Boolean isCanceledOnTouchOutSide){
        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        dialog = new Dialog(context,R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutSide);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
////        window.setGravity(Gravity.BOTTOM);
//        windowparams.height = ;
        windowparams.width = BaseAppUtil.getDeviceWidth(context)-2* DensityUtil.dip2px(context, 16);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        if(title!=null){
            tvTitle.setText(title);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(cancelText)){
            btcancel.setText(cancelText);
        }
        if(!TextUtils.isEmpty(confirmText)){
            btok.setText(confirmText);
        }

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(mlistener!=null){
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }


    public void showNewDialog(Context context, boolean showCancel, String title, String content,String cancelText, String confirmText, ConfirmDialogListener listener, Boolean isCanceledOnTouchOutSide){
        dismiss();
        this.mlistener=listener;
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        dialog = new Dialog(context,R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutSide);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.CENTER);
//        windowparams.height = 880;
//        windowparams.width = 740;
        windowparams.width = BaseAppUtil.getDeviceWidth(context)-2* DensityUtil.dip2px(context, 16);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btok = (TextView) dialogview.findViewById(R.id.confirm_tv);
        btcancel = (TextView) dialogview.findViewById(R.id.cancel_tv);
        tvTitle = (TextView) dialogview.findViewById(R.id.title);
        tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        if(title!=null){
            tvTitle.setText(title);
            tvTitle.setTextColor(0xffef3866);
            TextPaint tp = tvTitle.getPaint();
            tp.setFakeBoldText(true);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(showCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(cancelText)){
            btcancel.setText(cancelText);
            btcancel.setHeight(DensityUtil.dip2px(context,32));
            btcancel.setTextColor(0xfff03967);
            btcancel.setBackground(context.getResources().getDrawable(R.drawable.lucky_dialog_btn_left));
        }
        if(!TextUtils.isEmpty(confirmText)){
            btok.setText(confirmText);
            btok.setHeight(DensityUtil.dip2px(context,32));
            btok.setTextColor(context.getResources().getColor(R.color.white));
            btok.setBackground(context.getResources().getDrawable(R.drawable.lucky_dialog_btn_right));
        }

        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlistener!=null){
                    mlistener.ok();
                }
                dismiss();
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(mlistener!=null){
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mlistener!=null){
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void dismiss(){
        if(dialog !=null){
            dialog.dismiss();
            mlistener=null;
        }
    }
    public interface ConfirmDialogListener{
        void ok();
        void cancel();
        void dismiss();
    }
}
