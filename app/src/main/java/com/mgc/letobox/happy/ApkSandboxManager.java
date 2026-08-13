package com.mgc.letobox.happy;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.entity.pm.InstallResult;

/** Operations on the box-private virtual Android user. */
public final class ApkSandboxManager {
    public static final int USER_ID = 0;
    public static final String PACKAGE_NAME = "com.weewa.arrowc002";
    public static final String ASSET_NAME = "arrowc002.apk";
    public static final String GAME_NAME = "Arrow Puzzle C002";

    private ApkSandboxManager() {
    }

    public static boolean isInstalled() {
        return BlackBoxCore.get().isInstalled(PACKAGE_NAME, USER_ID);
    }

    public static InstallResult install(Context context) throws Exception {
        File sandboxInstallerDir = new File(context.getFilesDir(), "sandbox-installer");
        if (!sandboxInstallerDir.exists() && !sandboxInstallerDir.mkdirs()) {
            throw new IllegalStateException("无法创建盒子安装目录");
        }
        File apk = new File(sandboxInstallerDir, ASSET_NAME);
        copyAsset(context, apk);
        InstallResult result = BlackBoxCore.get().installPackageAsUser(apk, USER_ID);
        if (!result.success) {
            throw new IllegalStateException(TextUtils.isEmpty(result.msg) ? "沙盒安装失败" : result.msg);
        }
        if (!PACKAGE_NAME.equals(result.packageName)) {
            BlackBoxCore.get().uninstallPackageAsUser(result.packageName, USER_ID);
            throw new IllegalStateException("安装包名不匹配：" + result.packageName);
        }
        return result;
    }

    public static boolean launch() {
        return BlackBoxCore.get().launchApk(PACKAGE_NAME, USER_ID);
    }

    /** Stops only the virtual game process; the host box process remains alive. */
    public static void stopRunningGame() {
        BlackBoxCore.get().stopPackage(PACKAGE_NAME, USER_ID);
    }

    public static void uninstall() {
        BlackBoxCore.get().uninstallPackageAsUser(PACKAGE_NAME, USER_ID);
    }

    private static void copyAsset(Context context, File outputFile) throws Exception {
        try (InputStream input = context.getAssets().open(ASSET_NAME);
             FileOutputStream output = new FileOutputStream(outputFile, false)) {
            byte[] buffer = new byte[128 * 1024];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            output.getFD().sync();
        }
    }
}
