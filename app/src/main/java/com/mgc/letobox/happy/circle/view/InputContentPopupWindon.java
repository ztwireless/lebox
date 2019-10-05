package com.mgc.letobox.happy.circle.view;

/**
 * Create by zhaozhihui on 2018/9/5
 **/

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.mgc.letobox.happy.R;

public class InputContentPopupWindon extends PopupWindow {
    Context context;
    OnClickListener listener;
    View layout;
    Button btn;

    public InputContentPopupWindon(Context context, final OnClickListener listener) {
        this.context = context;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.input_layout, null);
        btn = (Button) layout.findViewById(R.id.send_btn);
        btn.setOnClickListener(listener);
        this.setContentView(layout);
        this.setWidth(LayoutParams.MATCH_PARENT);//设置宽度
        this.setHeight(LayoutParams.WRAP_CONTENT);//设置高度

        //设置弹出动画
        setAnimationStyle(R.anim.popup_show);
        //使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        setFocusable(true);
        //设置允许在外点击消失
        setOutsideTouchable(true);
        //这个是为了点击“Back”也能使其消失，不会影响背景
        setBackgroundDrawable(new BitmapDrawable());
        //显示在键盘上方
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
//      this.setBackgroundDrawable(dw);//设置背景

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                    //第二个参数标记是否是弹幕
                    listener.onClick(v);
                    dismiss();

            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                //AppUtils.hideSoftInput(context, popupCommentEdt.getWindowToken());
            }
        });

    }


}
