package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.app.Application;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.api.constant.Constant;
import com.ledong.lib.leto.listener.ILetoGameUpgradeListener;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.BenefitSettings_bubble;
import com.ledong.lib.leto.mgc.bean.BenefitSettings_shake;
import com.ledong.lib.leto.mgc.bean.BenefitSettings_upgrade;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.bean.GameLevelResultBean;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.ad.AdPreloader;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.statistic.GameStatisticManager;
import com.leto.game.base.statistic.StatisticEvent;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.bean.ShakeResultBean;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.util.LeBoxUtil;
import com.mgc.letobox.happy.view.FloatBubbleView;
import com.mgc.letobox.happy.view.ShakeShakeView;
import com.mgc.letobox.happy.view.UpgradeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;


public class FloatToolsCenter {
    private static final String TAG = FloatToolsCenter.class.getSimpleName();
    private static boolean TEST_ENV = false;

    private static int toInt(String text) {
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void init(final Application app) {
        TEST_ENV = BaseAppUtil.getMetaBooleanValue(app, "MGC_TEST_ENV");

        Leto.getInstance().addLetoLifecycleListener(new ILetoLifecycleListener() {
            @Override
            public void onLetoAppLaunched(final LetoActivity activity, String gameId) {
                if (isGameBubbleEnabled(gameId)) {
                    initBubbleTask(activity);
                }
                if (isGameShakeEnabled(gameId)) {
                    initShakeView(activity);
                }
            }

            @Override
            public void onLetoAppLoaded(LetoActivity letoActivity, String s) {

            }

            @Override
            public void onLetoAppShown(LetoActivity activity, String appId) {

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
                if (bubbleTimer != null) {
                    bubbleTimer.cancel();
                    bubbleTimer = null;
                }
                FloatViewManager.getInstance().removeUpgradeView(activity);
            }
        });


        Leto.getInstance().setGameUpgradeListener(new ILetoGameUpgradeListener() {
            @Override
            public void show(Activity activity, String gameId, Map<String, Integer> gameInfo, JSONObject params) {
                LetoTrace.d(TAG, "upgrade show");
                if (isGameUpgradeEnabled(gameId)) {
                    initUpgradeView(activity, gameId, params);

                    if (gameInfo != null && gameInfo.size() > 0) {
                        if (FloatViewManager.getInstance() != null) {
                            FloatViewManager.getInstance().notifyUpgrade(gameId, gameInfo);
                        }
                    }
                }
            }

            @Override
            public void hide(Activity context, String gameId) {
                FloatViewManager.getInstance().hideUpgradeView();
            }

            @Override
            public void popup(Activity context, String gameId) {

            }

            @Override
            public void notifyUpgrade(Activity context, String gameId, Map<String, Integer> gameInfo) {
                JSONArray jArray = new JSONArray();
                jArray.put(gameInfo);
                LetoTrace.d(TAG, String.format("upgrade notifyUpgrade gameId: %s, gameInfo: %s", gameId, jArray.toString()));

                if (FloatViewManager.getInstance() != null) {
                    FloatViewManager.getInstance().notifyUpgrade(gameId, gameInfo);
                }
            }
        });
    }

    private static boolean isGameBubbleEnabled(String gameId) {
        if (TEST_ENV) return true;
        if (!TextUtils.isEmpty(gameId) && MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getBubble() != null) {
            BenefitSettings_bubble bubble = MGCSharedModel.benefitSettings.getBubble();
            if (bubble.getIs_open() == 1 && bubble.getGame_ids() != null) {
                return bubble.getGame_ids().contains(gameId);
            }
        }
        return false;
    }

    private static boolean isGameShakeEnabled(String gameId) {
        if (TEST_ENV) return true;
        if (!TextUtils.isEmpty(gameId) &&  MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getShake() != null) {
            BenefitSettings_shake shake = MGCSharedModel.benefitSettings.getShake();
            if (shake.getIs_open() == 1 && shake.getGame_ids() != null) {
                return shake.getGame_ids().contains(gameId);
            }
        }
        return false;
    }

    private static boolean isGameUpgradeEnabled(String gameId) {
        if (TEST_ENV) return true;
        if (!TextUtils.isEmpty(gameId) && MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getUpgrade() != null) {
            BenefitSettings_upgrade update = MGCSharedModel.benefitSettings.getUpgrade();
            if (update.getIs_open() == 1 && update.getGame_ids() != null) {
                return update.getGame_ids().contains(gameId);
            }
        }
        return false;
    }

    private static long lastShakeTime = 0;

    private static void initShakeView(final LetoActivity activity) {
        if (MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getShake() != null && activity != null) {
            final BenefitSettings_shake shake = MGCSharedModel.benefitSettings.getShake();
            ShakeShakeView shakeView = FloatViewManager.getInstance().showShakeShake(activity, shake.getDefault_x(), shake.getDefault_y());
            if (shakeView == null) {
                return;
            }
            lastShakeTime = System.currentTimeMillis();
            shakeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int todayTimes = LeBoxSpUtil.todayShakeTimes(activity.getRunningGameId());
//                    Log.i(TAG, "click ShakeView " + activity.getRunningGameId() + "|" + LeBoxSpUtil.todayShakeTimes(activity.getRunningGameId()) + "|" + shake.max_times);
                    if (System.currentTimeMillis() - lastShakeTime < 600) {
                        // do nothing
                        return;
                    }

                    //点击上报
                    GameStatisticManager.statisticBenefitLog(activity, activity.getRunningGameId(), StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(), 0, 0, 0, 0, Constant.BENEFITS_TYPE_SHAKE, 0);

                    if (todayTimes >= shake.getMax_times()) {
                        lastShakeTime = System.currentTimeMillis();
                        Toast.makeText(activity, R.string.shake_time_used_out, Toast.LENGTH_SHORT).show();
                    } else {
                        lastShakeTime = System.currentTimeMillis();
                        LeBoxSpUtil.shakeOnce(activity.getRunningGameId());
                        Executors.newSingleThreadExecutor().submit(new Runnable() {
                            @Override
                            public void run() {
                                shakeIt(activity);
                            }
                        });
                    }
                }
            });
        }
    }

    private static void triggerJSShakeAwardEvent(LetoActivity activity, String awardId) {
        if (activity != null) {
            activity.notifyServiceSubscribeHandler("onAppShakeAward", String.format("{\"award_id\": \"%s\"}", awardId), 0);
        }
    }

    private static void shakeIt(final LetoActivity activity) {
        if (activity == null) {
            return;
        }
        String gameId = activity.getRunningGameId();


        LeBoxUtil.getShakeResult(activity, gameId, new HttpCallbackDecode<ShakeResultBean>(activity, null) {
            @Override
            public void onDataSuccess(ShakeResultBean shakeData) {
                if (shakeData == null || shakeData.add_coins == 0) {
                    // 由于服务器尚未有游戏奖励配置, 因此这里暂时写死, 25%插屏, 25%宝箱, 50%游戏金币
                    // 如果没有缓存好的插屏, 则跳过插屏
                    double f = Math.random();
                    if (f < 0.25 && AdPreloader.isInterstitialPreloaded()) {
                        final ApiContainer apiContainer = new ApiContainer(activity);
                        apiContainer.presentInterstitialAd(new ApiContainer.IApiResultListener() {
                            @Override
                            public void onApiSuccess(ApiContainer.ApiName apiName, Object o) {
                                Log.i(TAG, "onApiSuccess");
                                apiContainer.destroy();
                            }

                            @Override
                            public void onApiFailed(ApiContainer.ApiName apiName, boolean b) {
                                Log.i(TAG, "onApiFailed");
                                apiContainer.destroy();
                                Toast.makeText(activity, R.string.obtain_ad_failed, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (f < 0.75) {
                        triggerJSShakeAwardEvent(activity, "award1");
                    } else {
                        triggerJSShakeAwardEvent(activity, "award2");
                    }
                } else {
                    MGCDialogUtil.showMGCCoinDialog(activity, "", shakeData.add_coins, shakeData.add_coins_multiple, CoinDialogScene.SHAKE, new IMGCCoinDialogListener() {
                        @Override
                        public void onExit(boolean b, int i) {
                        }
                    });
                }
            }
        });
    }

    private static Timer bubbleTimer = null;

    private static void initBubbleTask(final LetoActivity activity) {
        if (MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getBubble() != null && activity != null) {
            final BenefitSettings_bubble bubble = MGCSharedModel.benefitSettings.getBubble();
            if (bubble.getCreate_interval() <= 0) return;

            final View.OnClickListener onBubbleClickListener = obtainBubbleClickListener(activity, bubble);

            if (bubbleTimer != null) bubbleTimer.cancel();

            bubbleTimer = new Timer();
            bubbleTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final Point position = randomPointIn(activity, bubble.getLeft_upper(), bubble.getLeft_lower(), bubble.getRight_upper(), bubble.getRight_lower());
                    final int count = randomIn(bubble.getMin_coins(), bubble.getMax_coins());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 由于服务器尚未有游戏奖励配置, 因此这里暂时写死, 25%提现金币, 25%宝箱, 50%游戏金币
                            double f = Math.random();
                            if (f < 0.25) {
//                                Log.i(TAG, "addBubble " + activity.getRunningGameId() + "|" + LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) + "|" + bubble.create_max_times);
                                if (FloatViewManager.getInstance().getBubbleCount() < bubble.screen_max_times
                                        && LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) < bubble.create_max_times) {
                                    FloatViewManager.getInstance().addBubble(activity, count, position.x, position.y, onBubbleClickListener);
                                    LeBoxSpUtil.bubbleOnce(activity.getRunningGameId());
                                }
                            } else if (f < 0.75) {
                                triggerJSShakeAwardEvent(activity, "award1");
                            } else {
                                triggerJSShakeAwardEvent(activity, "award2");
                            }
                        }
                    });
                }
            }, bubble.create_interval * 1000, bubble.create_interval * 1000);
        }
    }

    private static int randomIn(int min_coins, int max_coins) {
        return (int) (Math.random() * (max_coins - min_coins) + min_coins);
    }

    private static Point randomPointIn(LetoActivity activity, float left, float top, float right, float bottom) {
        int screenWidth = BaseAppUtil.getDeviceWidth(activity);
        int screenHeight = BaseAppUtil.getDeviceHeight(activity);
        int minX = (int) (left * screenWidth);
        int maxX = screenWidth - (int) (right * screenWidth);
        int minY = (int) (top * screenHeight);
        int maxY = screenHeight - (int) (bottom * screenHeight);
        return new Point(randomIn(minX, maxX), randomIn(minY, maxY));
    }

    private static View.OnClickListener obtainBubbleClickListener(final LetoActivity activity, final BenefitSettings_bubble bubble) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FloatBubbleView) {

                    //点击上报
                    GameStatisticManager.statisticBenefitLog(activity, activity.getRunningGameId(), StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(), 0, 0, 0, 0, Constant.BENEFITS_TYPE_BUBBLE, 0);

                    FloatBubbleView bubbleView = (FloatBubbleView) view;
                    FloatViewManager.getInstance().removeBubbleView(activity, bubbleView.getBubbleId());
                    MGCDialogUtil.showMGCCoinDialog(activity, "", bubbleView.getCoinCount(), bubble.coins_multiple, CoinDialogScene.BUBBLE, null);
                }
            }
        };
    }


    private static void initUpgradeView(Activity activity, String gameId, JSONObject params) {
        if (MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getUpgrade() != null && activity != null) {
            final BenefitSettings_upgrade update = MGCSharedModel.benefitSettings.getUpgrade();

            // 如果show里面指定了位置, 使用show的位置
            int xDirection = update.getDefault_x();
            float yRatio = update.getDefault_y();
            if(params != null) {
                String gravity = params.optString("gravity", "unspecified");
                if(!gravity.equals("unspecified")) {
                    xDirection = gravity.equals("left") ? 0 : 1;
                }
                if(params.has("percent_v")) {
                    yRatio = (float)params.optDouble("percent_v", update.getDefault_y());
                }
            }

            UpgradeView upgradeView = FloatViewManager.getInstance().showUpgradeView(activity, gameId, xDirection, yRatio);
            upgradeView.setGameId(gameId);
            upgradeView.getGameUpgradeSetting(activity, gameId);
            upgradeView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {
                    LetoTrace.d("UpgradeView", "click me......");

                    //点击上报
                    GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(), 0, 0, 0, 0, Constant.BENEFITS_TYPE_UPGRADE_GIFT, 0);

                    GameLevelResultBean.GameLevel levelReward = upgradeView.getRewardLevel();
                    if (levelReward != null) {
                        MGCDialogUtil.showRedEnvelopesDialog(activity, levelReward.getCoins(), update.getCoins_multiple(), levelReward.level_list_id, CoinDialogScene.GAME_UPGRADE, new IMGCCoinDialogListener() {
                            @Override
                            public void onExit(boolean video, int coinGot) {

                                if (upgradeView != null) {

                                    upgradeView.resetRewardStatus(levelReward.level_list_id);

                                    upgradeView.getGameUpgradeSetting(activity, gameId);
                                }
                            }
                        });
                    } else {
                        ToastUtil.s(activity, "请升级后再试");
                    }
                    return true;
                }
            });
        }
    }
}