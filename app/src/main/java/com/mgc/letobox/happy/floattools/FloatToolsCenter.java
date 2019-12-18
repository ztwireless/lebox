package com.mgc.letobox.happy.floattools;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.mgc.letobox.happy.view.FloatBubbleView;

public class FloatToolsCenter {
    public static void init(Application app) {
        Leto.getInstance().addLetoLifecycleListener(new ILetoLifecycleListener() {
            @Override
            public void onLetoAppLaunched(final LetoActivity activity, String s) {
                View.OnClickListener onBubbleClickListener = obtainBubbleClickListener(activity);

                FloatViewManager.getInstance().addBubble(activity, 444, 300, 1000, onBubbleClickListener);
                FloatViewManager.getInstance().addBubble(activity, 444, 300, 500, onBubbleClickListener);
                FloatViewManager.getInstance().addBubble(activity, 444, 200, 200, onBubbleClickListener);
                FloatViewManager.getInstance().addBubble(activity, 444, 800, 1000, onBubbleClickListener);
                FloatViewManager.getInstance().addBubble(activity, 444, 444, 244, onBubbleClickListener);
                FloatViewManager.getInstance().showShakeShake(activity, 300, 1000);
            }

            @Override
            public void onLetoAppLoaded(LetoActivity letoActivity, String s) {

            }

            @Override
            public void onLetoAppPaused(LetoActivity letoActivity, String s) {

            }

            @Override
            public void onLetoAppResumed(LetoActivity letoActivity, String s) {

            }

            @Override
            public void onLetoAppExit(LetoActivity activity, String s) {
                FloatViewManager.getInstance().removeAllBubbleViews(activity);
                FloatViewManager.getInstance().removeShakeView(activity);
            }
        });
    }

    private static View.OnClickListener obtainBubbleClickListener(final LetoActivity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FloatBubbleView) {
                    FloatBubbleView bubbleView = (FloatBubbleView) view;
                    FloatViewManager.getInstance().removeBubbleView(activity, bubbleView.getBubbleId());
                    MGCDialogUtil.showMGCCoinDialog(activity, "", bubbleView.getCoinCount(), 3, CoinDialogScene.BUBBLE, new IMGCCoinDialogListener() {
                        @Override
                        public void onExit(boolean b, int i) {
                            Toast.makeText(activity, "onExit", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
    }
}
