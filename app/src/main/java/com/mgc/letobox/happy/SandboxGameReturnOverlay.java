package com.mgc.letobox.happy;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback;

/** Adds a host-controlled return affordance over the sandboxed demo game. */
final class SandboxGameReturnOverlay {
    private static final String TAG = "SandboxReturnOverlay";
    private static final String OVERLAY_TAG = "lebox:sandbox:return";
    private static final String UNITY_ACTIVITY = "com.unity3d.player.UnityPlayerGameActivity";
    private static final long STOP_GAME_DELAY_MS = 600L;

    private SandboxGameReturnOverlay() {
    }

    static AppLifecycleCallback createCallback() {
        return new AppLifecycleCallback() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addToArrowGame(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                addToArrowGame(activity);
            }
        };
    }

    private static void addToArrowGame(final Activity activity) {
        if (!UNITY_ACTIVITY.equals(activity.getClass().getName())
                || !ApkSandboxManager.PACKAGE_NAME.equals(activity.getPackageName())) {
            return;
        }
        final Window window = activity.getWindow();
        if (window == null) {
            return;
        }
        final View decor = window.getDecorView();
        if (!(decor instanceof ViewGroup)) {
            return;
        }
        final ViewGroup root = (ViewGroup) decor;
        if (root.findViewWithTag(OVERLAY_TAG) != null) {
            return;
        }

        TextView back = new TextView(activity);
        back.setTag(OVERLAY_TAG);
        back.setText("‹  返回盒子");
        back.setTextColor(Color.WHITE);
        back.setTextSize(16);
        back.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        back.setGravity(Gravity.CENTER);
        back.setElevation(dp(activity, 16));
        back.setPadding(dp(activity, 16), 0, dp(activity, 16), 0);

        GradientDrawable background = new GradientDrawable();
        background.setColor(0xCC222222);
        background.setCornerRadius(dp(activity, 24));
        background.setStroke(dp(activity, 1), 0x99FFFFFF);
        back.setBackground(background);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(activity, 48), Gravity.TOP | Gravity.START);
        params.leftMargin = dp(activity, 12);
        params.topMargin = dp(activity, 28);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                // Unity 6 GameActivity may block indefinitely in
                // GameActivity.terminateNativeCode() when finish() drives onDestroy inside
                // the sandbox. Put the proxy task behind the already-running box task first,
                // then kill only the virtual game process without invoking Unity onDestroy.
                ignored.setEnabled(false);
                boolean movedToBack = activity.moveTaskToBack(true);
                Log.i(TAG, "Return requested; game task moved to back=" + movedToBack);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Stopping sandbox game process after returning to box");
                        ApkSandboxManager.stopRunningGame();
                    }
                }, STOP_GAME_DELAY_MS);
            }
        });
        root.addView(back, params);
        back.bringToFront();
    }

    private static int dp(Activity activity, int value) {
        return Math.round(value * activity.getResources().getDisplayMetrics().density);
    }
}
