package com.mgc.letobox.happy.circle.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.view.InputContentPopupWindon;
import com.mgc.letobox.happy.circle.view.KeyboardDetectorRelativeLayout;

/**
 * Created by DELL on 2018/7/3.
 */

public class CircleDialogUtils {

    private static Toast mToast;

    KeyboardDetectorRelativeLayout root_layout;
    EditText et;
    Button btn;
    InputContentPopupWindon popu;


    public static void createToast(Context context, String titleName, String text) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
        TextView textView = view.findViewById(R.id.textView);
        TextView textViewTitle = view.findViewById(R.id.title);

        if (titleName != null && !titleName.isEmpty()) {
            textViewTitle.setText(titleName);
        }

        if (text != null && !text.isEmpty()) {
            textView.setText(text);
        }

        if (mToast == null) {
            mToast = new Toast(context);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setView(view);
        mToast.show();
    }

    public static void showPopWindow(Context context, int count, View mView, int with, final PopWindowCallBack popWindowCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.window_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, DensityUtil.dip2px(context, 100), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popmenu_animation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        TextView textView1 = view.findViewById(R.id.textView1);
        View textViewLine = view.findViewById(R.id.textViewLine);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        View viewLine = view.findViewById(R.id.viewLine);

        if (count == 0) {
            textView3.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {
            textView3.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(mView);
        } else {
            int[] location = new int[2];
            mView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, with, y + mView.getHeight());
        }

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow1(popupWindow);
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow2(popupWindow);
                }
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow3(popupWindow);
                }
            }
        });
    }

    public static void showArticlePopWindow(Context context, boolean isSelf, View mView, int with, final PopWindowCallBack popWindowCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.window_layout, null);
        final PopupWindow popupWindow = new PopupWindow(view, DensityUtil.dip2px(context, 100), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popmenu_animation);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        TextView textView1 = view.findViewById(R.id.textView1);
        View textViewLine = view.findViewById(R.id.textViewLine);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        View viewLine = view.findViewById(R.id.viewLine);

        textView1.setText("分享");
        textView2.setText("编辑");
        textView3.setText("删除");

        if (!isSelf) {
            textView3.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            textViewLine.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT < 24) {
            popupWindow.showAsDropDown(mView);
        } else {
            int[] location = new int[2];
            mView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, with, y + mView.getHeight());
        }

        textView1.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow1(popupWindow);
                }
                return true;
            }
        });
        textView2.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow2(popupWindow);
                }
                return true;
            }
        });
        textView3.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (popWindowCallBack != null) {
                    popWindowCallBack.popWindow3(popupWindow);
                }
                return true;
            }
        });
    }


    public static void showCircle(Context context, View mView, int editext, int detele, final ShowCircleCallBack showCircleCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.circle_right_window, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

        TextView textViewBianji = view.findViewById(R.id.textViewBianji);
        TextView textViewDetele = view.findViewById(R.id.textViewDetele);
        View textViewLine = view.findViewById(R.id.textViewLine);

        if (editext == 1) {
            textViewBianji.setVisibility(View.VISIBLE);
        } else {
            textViewBianji.setVisibility(View.GONE);
            textViewLine.setVisibility(View.GONE);
        }

        if (detele == 1) {
            textViewDetele.setVisibility(View.VISIBLE);
        } else {
            textViewDetele.setVisibility(View.GONE);
            textViewLine.setVisibility(View.GONE);
        }

        int[] location = new int[2];
        mView.getLocationOnScreen(location);
        popupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, location[0] + mView.getWidth() - 50, location[1] + 5);

        textViewBianji.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                showCircleCallBack.BianJi();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
        });

        textViewDetele.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                showCircleCallBack.Detele();
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return true;
            }
        });
    }

    public static void showDialog(Context context, String title, String content, String left, String right, final ShowDialogCallBack listener) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        View dialogview = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_bg_style);
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(false);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
        windowparams.width = BaseAppUtil.getDeviceWidth(context) - 2 * DensityUtil.dip2px(context, 16);

        TextView confirm_tv = (TextView) dialogview.findViewById(R.id.confirm_tv);
        TextView cancel_tv = (TextView) dialogview.findViewById(R.id.cancel_tv);
        TextView tvTitle = (TextView) dialogview.findViewById(R.id.title);
        TextView tvContent = (TextView) dialogview.findViewById(R.id.tv_content);
        if (title != null) {
            tvTitle.setText(title);
        }
        if (content != null) {
            tvContent.setText(content);
        }
        cancel_tv.setText(left);
        confirm_tv.setText(right);
        confirm_tv.setTextColor(context.getResources().getColor(R.color.text_gray));
        cancel_tv.setBackgroundResource(R.drawable.shape_circle_rect_white_bolder);
        confirm_tv.setBackgroundResource(R.drawable.shape_circle_rect_white_bolder);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.LeftClick(dialog);
            }
        });

        confirm_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.RightClick(dialog);
            }
        });
        dialog.show();
    }

    public static void fillDialog(Context context, Boolean isShow, final FillDialogCallBack fillDialogCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        final Dialog dialog = new Dialog(context, R.style.CornersDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.circle_fill_dialog, null);
        ImageView imageBack = mView.findViewById(R.id.imageBack);
        TextView textViewCreate = mView.findViewById(R.id.textViewCreate);
        final StarBar starBar = mView.findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        final EditText editText = mView.findViewById(R.id.editText);
        TextView textView_bar = mView.findViewById(R.id.textView_bar);

        if (isShow) {
            starBar.setVisibility(View.VISIBLE);
            textView_bar.setVisibility(View.VISIBLE);
        } else {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
        }

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(mView, layoutParams);
        dialog.show();

        imageBack.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        textViewCreate.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                fillDialogCallBack.textViewCreate(dialog, starBar, editText);
                return true;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillDialogCallBack.editText(editText);
            }
        });

    }

    public static void fillDialog(Context context, String title, Boolean isShow, final FillDialogCallBack fillDialogCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        final Dialog dialog = new Dialog(context, R.style.CornersDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.circle_fill_dialog, null);
        ImageView imageBack = mView.findViewById(R.id.imageBack);

        TextView textViewCreate = mView.findViewById(R.id.textViewCreate);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        final StarBar starBar = mView.findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        final EditText editText = mView.findViewById(R.id.editText);
        TextView textView_bar = mView.findViewById(R.id.textView_bar);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        if (isShow) {
            starBar.setVisibility(View.VISIBLE);
            textView_bar.setVisibility(View.VISIBLE);
        } else {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
        }

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(mView, layoutParams);
        dialog.show();

        imageBack.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        textViewCreate.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                fillDialogCallBack.textViewCreate(dialog, starBar, editText);
                return true;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillDialogCallBack.editText(editText);
            }
        });
    }

    public static void fillRatingDialog(Context context, Boolean isShow, Boolean enableChange, int score, final FillDialogCallBack fillDialogCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        final Dialog dialog = new Dialog(context, R.style.CornersDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.circle_fill_dialog, null);
        ImageView imageBack = mView.findViewById(R.id.imageBack);

        TextView textViewCreate = mView.findViewById(R.id.textViewCreate);
        final StarBar starBar = mView.findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        final EditText editText = mView.findViewById(R.id.editText);
        TextView textView_bar = mView.findViewById(R.id.textView_bar);

        if (isShow) {
            starBar.setVisibility(View.VISIBLE);
            textView_bar.setVisibility(View.VISIBLE);

            if (!enableChange) {
                starBar.setIsIndicator(true);
            }
            starBar.setMark(score);
        } else {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
        }

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, height);
        dialog.setContentView(mView, layoutParams);
        dialog.show();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface d, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    fillDialogCallBack.cancel();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface d) {
//                fillDialogCallBack.cancel();
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
//            }
//        });
        imageBack.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                fillDialogCallBack.cancel();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        textViewCreate.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                fillDialogCallBack.textViewCreate(dialog, starBar, editText);
                return true;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fillDialogCallBack.editText(editText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fillDialogCallBack.editText(editText);
            }
        });
    }

    public static void CircleDialog(Context context, String text1, String text2, final DialogCallBack callBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.circle_dialog, null);
        TextView textView1 = mView.findViewById(R.id.textView1);
        TextView textView2 = mView.findViewById(R.id.textView2);
        TextView textView3 = mView.findViewById(R.id.textView3);
        textView1.setText(text1);
        textView2.setText(text2);
        dialog.setContentView(mView);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 10;
        dialogWindow.setAttributes(lp);
        dialog.show();

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    callBack.dialogClick1(dialog);
                }
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    callBack.dialogClick2(dialog);
                }
            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public interface ShowCircleCallBack {
        void BianJi();

        void Detele();
    }

    public interface FillDialogCallBack {
        void textViewCreate(Dialog dialog, StarBar starBar, EditText editText);

        void editText(EditText editText);

        void cancel();
    }

    public interface ShowDialogCallBack {
        void LeftClick(Dialog dialog);

        void RightClick(Dialog dialog);
    }

    public interface DialogCallBack {
        void dialogClick1(Dialog dialog);

        void dialogClick2(Dialog dialog);
    }

    public interface PopWindowCallBack {
        void popWindow1(PopupWindow popupWindow);

        void popWindow2(PopupWindow popupWindow);

        void popWindow3(PopupWindow popupWindow);
    }
}
