package com.mgc.letobox.happy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ledong.lib.leto.trace.LetoTrace;
import com.leto.game.base.listener.IProgressListener;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.bean.VersionResultBean;
import com.mgc.letobox.happy.util.UpdateUtil;


public class VersionDialog {
    private static final String TAG = VersionDialog.class.getSimpleName();
    private Dialog dialog;
    private ConfirmDialogListener mlistener;

    TextView tvTitle;
    TextView tvContent;
    TextView tvDownload;
    ImageView btcancel;
    ImageView titleBg;
    ProgressBar progressBar;
    FrameLayout donwloadLayout;

    View.OnClickListener downloadListener;

    UpdateUtil mUpdateUtil;


    public void showDialog(final Context context, final VersionResultBean version, ConfirmDialogListener listener) {

        dismiss();
        final boolean isCanCancel = version.getType() != 1 ? true : false;
        this.mlistener = listener;
        View dialogview = LayoutInflater.from(context).inflate(MResource.getIdByName(context, "R.layout.dialog_common_version"), null);
        dialog = new Dialog(context, MResource.getIdByName(context, "R.style.LetoCustomDialog"));
        //设置view
        dialog.setContentView(dialogview);
        dialog.setCanceledOnTouchOutside(isCanCancel);
        dialog.setCancelable(isCanCancel);
        //dialog默认是环绕内容的
        //通过window来设置位置、高宽
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowparams = window.getAttributes();
////        window.setGravity(Gravity.BOTTOM);
//        windowparams.height = ;
        windowparams.width = DensityUtil.dip2px(context, 245);
//        设置背景透明,但是那个标题头还是在的，只是看不见了
        //注意：不设置背景，就不能全屏
//        window.setBackgroundDrawableResource(android.R.color.transparent);

        btcancel = (ImageView) dialogview.findViewById(MResource.getIdByName(context, "R.id.cancel_tv"));
        tvTitle = (TextView) dialogview.findViewById(MResource.getIdByName(context, "R.id.title"));
        tvContent = (TextView) dialogview.findViewById(MResource.getIdByName(context, "R.id.content"));
        titleBg = (ImageView) dialogview.findViewById(MResource.getIdByName(context, "R.id.lebox_upgrade_title_bg"));
        tvDownload = (TextView) dialogview.findViewById(MResource.getIdByName(context, "R.id.btn_download"));
        progressBar = (ProgressBar) dialogview.findViewById(MResource.getIdByName(context, "R.id.progressBar"));
        donwloadLayout = (FrameLayout) dialogview.findViewById(MResource.getIdByName(context, "R.id.layout_dialog"));
        tvDownload.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);


        GlideUtil.loadRoundedCorner(context, R.mipmap.lebox_upgrade_title, titleBg, 6, true, true, false, false);

        if (version != null) {
            tvTitle.setText("升级上线");
        }
        if (version != null && !TextUtils.isEmpty(version.getContent())) {
            tvContent.setText(version.getContent());
        }

        if(isCanCancel){
            btcancel.setVisibility(View.VISIBLE);
        }else{
            btcancel.setVisibility(View.GONE);
        }

        btcancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mlistener != null) {
                                                mlistener.cancel();
                                            }
                                            dismiss();
                                        }
                                    }
        );

        downloadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpdateUtil != null) {

                    if(mUpdateUtil.isDownload()){
                        tvDownload.setText("安装");
                        if (!mUpdateUtil.isCancel()) {
                            mUpdateUtil.installApk();
                        }
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        tvDownload.setOnClickListener(null);
                        mUpdateUtil.downloadApk(context, new IProgressListener() {
                            @Override
                            public void onProgressUpdate(final long progress, long totalBytesWritten, long totalBytesExpectedToWrite) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        LetoTrace.d(TAG, "progress: " + progress);
                                        try {
                                            progressBar.setProgress((int) progress);
                                            tvDownload.setText("" + progress + "%");
                                            if (progress == 100) {
                                                if (!mUpdateUtil.isCancel()) {
                                                    mUpdateUtil.installApk();
                                                }
                                                tvDownload.setText("安装");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void abort() {

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        LetoTrace.d(TAG, "download abort....");
                                        try {
                                            progressBar.setProgress(0);
                                            progressBar.setVisibility(View.GONE);
                                            tvDownload.setText("重新下载");
                                            tvDownload.setOnClickListener(downloadListener);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                    }

                }
            }
        };


        donwloadLayout.setOnClickListener(downloadListener);

        mUpdateUtil = new UpdateUtil(context);
        mUpdateUtil.setVersion(version);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                mUpdateUtil.setCancel(true);

                if (mlistener != null) {
                    mlistener.dismiss();
                }
            }
        });

        dialog.show();
    }

    public boolean isShowing(){
        if (dialog != null &&  dialog.isShowing()) {
           return  true;
        }
        return false;
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            mlistener = null;
        }
    }

    public interface ConfirmDialogListener {
        void ok();

        void cancel();

        void dismiss();
    }
}
