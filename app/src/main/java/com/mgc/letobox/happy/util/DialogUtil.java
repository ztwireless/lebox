package com.mgc.letobox.happy.util;

import android.content.Context;
import android.content.DialogInterface;

import com.leto.game.base.util.FileUtil;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.dialog.GeneralDialog;
import com.mgc.letobox.happy.dialog.InputDialog;
import com.mgc.letobox.happy.dialog.WebDialog;

public class DialogUtil {
    public static void showErrorDialog(Context ctx, String msg) {
        GeneralDialog d = new GeneralDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_info_dialog_title")),
            msg);
        d.setCancelable(false);
        d.show();
    }

    public static void showErrorDialog(Context ctx, String msg, DialogInterface.OnClickListener listener) {
        GeneralDialog d = new GeneralDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_info_dialog_title")),
            msg,
            false,
            listener);
        d.show();
    }

    public static void showInfoDialog(Context ctx, String msg) {
        GeneralDialog d = new GeneralDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_info_dialog_title")),
            msg);
        d.setCancelable(false);
        d.show();
    }

    public static void showInfoDialog(Context ctx, String title, String msg) {
        GeneralDialog d = new GeneralDialog(ctx, title, msg);
        d.setCancelable(false);
        d.show();
    }

    public static void showConfirmDialog(Context ctx, String msg, DialogInterface.OnClickListener listener) {
        GeneralDialog d = new GeneralDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_info_dialog_title")),
            msg,
            true,
            listener);
        d.setOkButtonText(ctx.getString(MResource.getIdByName(ctx, "R.string.confirm")));
        d.show();
    }

    public static void showRetryDialog(Context ctx, String msg, DialogInterface.OnClickListener listener) {
        GeneralDialog d = new GeneralDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_info_dialog_title")),
            msg,
            true,
            listener);
        d.setOkButtonText(ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_retry")));
        d.show();
    }

    public static void showInputDialog(Context ctx, String text, DialogInterface.OnClickListener listener) {
        InputDialog d = new InputDialog(ctx,
            ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_input_dialog_title")),
            text,
            true,
            listener);
        d.show();
    }

    public static void showAgreement(Context ctx, String path) {
        String data = path.startsWith("http") ? path : FileUtil.readAssetsFileContent(ctx, path);
        WebDialog d = new WebDialog(ctx,
                ctx.getString(MResource.getIdByName(ctx, "R.string.lebox_web_dialog_title")),
                data);
        d.show();
    }
}
