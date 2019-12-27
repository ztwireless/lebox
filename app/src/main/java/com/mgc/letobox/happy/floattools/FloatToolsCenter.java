package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.app.Application;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.LetoConst;
import com.ledong.lib.leto.LetoScene;
import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.api.constant.Constant;
import com.ledong.lib.leto.listener.ILetoGameUpgradeListener;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.bean.GameLevelResultBean;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.ad.AdPreloader;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.statistic.GameStatisticManager;
import com.leto.game.base.statistic.StatisticEvent;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.model.FloatToolsConfig;
import com.mgc.letobox.happy.model.ShakeResult;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.view.FloatBubbleView;
import com.mgc.letobox.happy.view.ShakeShakeView;
import com.mgc.letobox.happy.view.UpgradeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FloatToolsCenter {
    private static final String TAG = FloatToolsCenter.class.getSimpleName();
    private static FloatToolsConfig floatToolsConfig;
    private static boolean TEST_ENV = false;

    private static int toInt(String text) {
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            return 0;
        }
    }

    private static Retrofit buildRetrofit() {
        Log.i(TAG, "buildRetrofit " + TEST_ENV);
        return new Retrofit.Builder()
                .baseUrl(TEST_ENV ? LeBoxConstant.MGCServerUrlDev : LeBoxConstant.MGCServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void init(final Application app) {
        TEST_ENV = BaseAppUtil.getMetaBooleanValue(app, "MGC_TEST_ENV");
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                MGCService service = buildRetrofit().create(MGCService.class);
                Call<FloatToolsConfig> configCall = service.obtainFloatToolsConfig(toInt(BaseAppUtil.getChannelID(app)),
                        LoginManager.getUserId(app), Leto.getFrameworkVersion(), Leto.getVersion(), LetoConst.SDK_OPEN_TOKEN);
                try {
                    Response<FloatToolsConfig> response = configCall.execute();
                    floatToolsConfig = response.body();
                    if (TEST_ENV) {
                        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getBubble() != null) {
                            floatToolsConfig.getData().getBubble().create_interval = 1;
                            floatToolsConfig.getData().getBubble().create_max_times = 500;
                            floatToolsConfig.getData().getBubble().screen_max_times = 6;
                        }
                        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getShake() != null) {
                            floatToolsConfig.getData().getShake().default_x = 1;
                            floatToolsConfig.getData().getShake().default_y = 0.8f;
                            floatToolsConfig.getData().getShake().max_times = 15;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
            public void show(Activity activity, String gameId, Map<String, Integer> gameInfo, JSONObject jsonObject) {
                LetoTrace.d(TAG, "upgrade show");
                if (isGameUpgradeEnabled(gameId)) {
                    initUpgradeView(activity, gameId);

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
        int gameIdInt = toInt(gameId);
        if (gameIdInt != 0 && floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getBubble() != null) {
            FloatToolsConfig.Data.Bubble bubble = floatToolsConfig.getData().getBubble();
            if (bubble.is_open == 1 && bubble.game_ids != null) {
                return bubble.game_ids.contains(gameIdInt);
            }
        }
        return false;
    }

    private static boolean isGameShakeEnabled(String gameId) {
        if (TEST_ENV) return true;
        int gameIdInt = toInt(gameId);
        if (gameIdInt != 0 && floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getShake() != null) {
            FloatToolsConfig.Data.Shake shake = floatToolsConfig.getData().getShake();
            if (shake.is_open == 1 && shake.game_ids != null) {
                return shake.game_ids.contains(gameIdInt);
            }
        }
        return false;
    }

    private static boolean isGameUpgradeEnabled(String gameId) {
        if (TEST_ENV) return true;
        int gameIdInt = toInt(gameId);
        if (gameIdInt != 0 && floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getUpgrade() != null) {
            FloatToolsConfig.Data.Upgrade update = floatToolsConfig.getData().getUpgrade();
            if (update.is_open == 1 && update.game_ids != null) {
                return update.game_ids.contains(gameIdInt);
            }
        }
        return false;
    }

    private static long lastShakeTime = 0;

    private static void initShakeView(final LetoActivity activity) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getShake() != null) {
            final FloatToolsConfig.Data.Shake shake = floatToolsConfig.getData().getShake();
            ShakeShakeView shakeView = FloatViewManager.getInstance().showShakeShake(activity, shake.default_x, shake.default_y);
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
                    GameStatisticManager.statisticBenefitLog(activity, activity.getRunningGameId(), StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(),0, 0, 0, 0, Constant.BENEFITS_TYPE_SHAKE, 0);

                    if (todayTimes >= shake.max_times) {
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
        if(activity != null) {
            activity.notifyServiceSubscribeHandler("onAppShakeAward", String.format("{\"award_id\": \"%s\"}", awardId), 0);
        }
    }

    private static void shakeIt(final LetoActivity activity) {
        String gameId = activity.getRunningGameId();
//        Log.i(TAG, "gameId " + gameId);
        MGCService service = buildRetrofit().create(MGCService.class);
        Call<ShakeResult> shakeResultCall = service.obtainShakeResult(
                toInt(BaseAppUtil.getChannelID(activity)), toInt(gameId), LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN);
        try {
            Response<ShakeResult> shakeResultResponse = shakeResultCall.execute();
            if (shakeResultResponse != null) {
                final ShakeResult shakeResult = shakeResultResponse.body();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShakeResult.Data shakeData = shakeResult.getData();
                        if (shakeData == null || shakeData.add_coins == 0) {
                            // 由于服务器尚未有游戏奖励配置, 因此这里暂时写死, 25%插屏, 25%宝箱, 50%游戏金币
                            // 如果没有缓存好的插屏, 则跳过插屏
                            double f = Math.random();
//                            if(f < 0.25 && AdPreloader.isInterstitialPreloaded()) {
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
//                            } else if(f < 0.75) {
//                                triggerJSShakeAwardEvent(activity, "award1");
//                            } else {
//                                triggerJSShakeAwardEvent(activity, "award2");
//                            }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Timer bubbleTimer = null;

    private static void initBubbleTask(final LetoActivity activity) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getBubble() != null) {
            final FloatToolsConfig.Data.Bubble bubble = floatToolsConfig.getData().getBubble();
            if (bubble.create_interval <= 0) return;

            final View.OnClickListener onBubbleClickListener = obtainBubbleClickListener(activity, bubble);

            if (bubbleTimer != null) bubbleTimer.cancel();

            bubbleTimer = new Timer();
            bubbleTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final Point position = randomPointIn(activity, bubble.left_upper, bubble.left_lower, bubble.right_upper, bubble.right_lower);
                    final int count = randomIn(bubble.min_coins, bubble.max_coins);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 由于服务器尚未有游戏奖励配置, 因此这里暂时写死, 25%提现金币, 25%宝箱, 50%游戏金币
                            double f = Math.random();
//                            if(f < 0.25) {
//                                Log.i(TAG, "addBubble " + activity.getRunningGameId() + "|" + LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) + "|" + bubble.create_max_times);
                                if (FloatViewManager.getInstance().getBubbleCount() < bubble.screen_max_times
                                    && LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) < bubble.create_max_times) {
                                    FloatViewManager.getInstance().addBubble(activity, count, position.x, position.y, onBubbleClickListener);
                                    LeBoxSpUtil.bubbleOnce(activity.getRunningGameId());
                                }
//                            } else if(f < 0.75) {
//                                triggerJSShakeAwardEvent(activity, "award1");
//                            } else {
//                                triggerJSShakeAwardEvent(activity, "award2");
//                            }
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

    private static View.OnClickListener obtainBubbleClickListener(final LetoActivity activity, final FloatToolsConfig.Data.Bubble bubble) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FloatBubbleView) {

                    //点击上报
                    GameStatisticManager.statisticBenefitLog(activity, activity.getRunningGameId(), StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(),0, 0, 0, 0, Constant.BENEFITS_TYPE_BUBBLE, 0);

                    FloatBubbleView bubbleView = (FloatBubbleView) view;
                    FloatViewManager.getInstance().removeBubbleView(activity, bubbleView.getBubbleId());
                    MGCDialogUtil.showMGCCoinDialog(activity, "", bubbleView.getCoinCount(), bubble.coins_multiple, CoinDialogScene.BUBBLE, null);
                }
            }
        };
    }


    private static void initUpgradeView(Activity activity, String gameId) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getUpgrade() != null) {
            final FloatToolsConfig.Data.Upgrade update = floatToolsConfig.getData().getUpgrade();
            UpgradeView upgradeView = FloatViewManager.getInstance().showUpgradeView(activity, gameId, update.default_x, update.default_y);
            upgradeView.setGameId(gameId);
            upgradeView.getGameUpgradeSetting(activity, gameId);
            upgradeView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {
                    LetoTrace.d("UpgradeView", "click me......");

                    //点击上报
                    GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal(),0, 0, 0, 0, Constant.BENEFITS_TYPE_UPGRADE_GIFT, 0);

                    GameLevelResultBean.GameLevel levelReward = upgradeView.getRewardLevel();
                    if (levelReward != null) {
                        MGCDialogUtil.showRedEnvelopesDialog(activity, levelReward.getCoins(), update.coins_multiple, levelReward.level_list_id, CoinDialogScene.GAME_UPGRADE, new IMGCCoinDialogListener() {
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