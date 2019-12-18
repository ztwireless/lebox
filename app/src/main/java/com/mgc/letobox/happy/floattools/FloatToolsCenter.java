package com.mgc.letobox.happy.floattools;

import android.app.Application;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.LetoConst;
import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.BuildConfig;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.model.FloatToolsConfig;
import com.mgc.letobox.happy.model.ShakeResult;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.view.FloatBubbleView;
import com.mgc.letobox.happy.view.ShakeShakeView;

import java.io.IOException;
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

    private static int toInt(String text) {
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            return 0;
        }
    }

    private static Retrofit buildRetrofit() {
        Log.i(TAG, "buildRetrofit " + BuildConfig.DEBUG);
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.DEBUG ? LeBoxConstant.MGCServerUrlDev : LeBoxConstant.MGCServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void init(final Application app) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                MGCService service = buildRetrofit().create(MGCService.class);
                Call<FloatToolsConfig> configCall = service.obtainFloatToolsConfig(toInt(BaseAppUtil.getChannelID(app)),
                        LoginManager.getUserId(app), Leto.getFrameworkVersion(), Leto.getVersion(), LetoConst.SDK_OPEN_TOKEN);
                try {
                    Response<FloatToolsConfig> response = configCall.execute();
                    floatToolsConfig = response.body();
                    if (BuildConfig.DEBUG) {
                        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getBubble() != null) {
                            floatToolsConfig.getData().getBubble().create_interval = 1000;
                            floatToolsConfig.getData().getBubble().create_max_times = 15;
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
            public void onLetoAppLaunched(final LetoActivity activity, String s) {
                String gameId = activity.getRunningGameId();
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
            }
        });
    }

    private static boolean isGameBubbleEnabled(String gameId) {
        if (BuildConfig.DEBUG) return true;
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
        if (BuildConfig.DEBUG) return true;
        int gameIdInt = toInt(gameId);
        if (gameIdInt != 0 && floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getShake() != null) {
            FloatToolsConfig.Data.Shake shake = floatToolsConfig.getData().getShake();
            if (shake.is_open == 1 && shake.game_ids != null) {
                return shake.game_ids.contains(gameIdInt);
            }
        }
        return false;
    }

    private static long lastShakeTime = 0;

    private static void initShakeView(LetoActivity activity) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getShake() != null) {
            FloatToolsConfig.Data.Shake shake = floatToolsConfig.getData().getShake();
            ShakeShakeView shakeView = FloatViewManager.getInstance().showShakeShake(activity, shake.default_x, shake.default_y);
            lastShakeTime = System.currentTimeMillis();
            shakeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int todayTimes = LeBoxSpUtil.todayShakeTimes(activity.getRunningGameId());
//                    Log.i(TAG, "click ShakeView " + activity.getRunningGameId() + "|" + LeBoxSpUtil.todayShakeTimes(activity.getRunningGameId()) + "|" + shake.max_times);
                    if (System.currentTimeMillis() - lastShakeTime < 600) {
                        // do nothing
                    } else if (todayTimes >= shake.max_times) {
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

    private static void shakeIt(LetoActivity activity) {
        String gameId = activity.getRunningGameId();
//        Log.i(TAG, "gameId " + gameId);
        MGCService service = buildRetrofit().create(MGCService.class);
        Call<ShakeResult> shakeResultCall = service.obtainShakeResult(
                toInt(BaseAppUtil.getChannelID(activity)), toInt(gameId), LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN);
        try {
            Response<ShakeResult> shakeResultResponse = shakeResultCall.execute();
            if (shakeResultResponse != null) {
                ShakeResult shakeResult = shakeResultResponse.body();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (shakeResult.add_coins == 0) {
                            ApiContainer apiContainer = new ApiContainer(activity);
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
                        } else {
                            MGCDialogUtil.showMGCCoinDialog(activity, "", shakeResult.add_coins, shakeResult.add_coins_multiple, CoinDialogScene.SHAKE, new IMGCCoinDialogListener() {
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

    private static void initBubbleTask(LetoActivity activity) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getBubble() != null) {
            FloatToolsConfig.Data.Bubble bubble = floatToolsConfig.getData().getBubble();
            if (bubble.create_interval <= 0) return;

            View.OnClickListener onBubbleClickListener = obtainBubbleClickListener(activity, bubble);

            if (bubbleTimer != null) bubbleTimer.cancel();

            bubbleTimer = new Timer();
            bubbleTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Point position = randomPointIn(activity, bubble.left_upper, bubble.left_lower, bubble.right_upper, bubble.right_lower);
                    int count = randomIn(bubble.min_coins, bubble.max_coins);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                Log.i(TAG, "addBubble " + activity.getRunningGameId() + "|" + LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) + "|" + bubble.create_max_times);
                            if (LeBoxSpUtil.todayBubbleTimes(activity.getRunningGameId()) < bubble.create_max_times) {
                                FloatViewManager.getInstance().addBubble(activity, count, position.x, position.y, onBubbleClickListener);
                                LeBoxSpUtil.bubbleOnce(activity.getRunningGameId());
                            }
                        }
                    });
                }
            }, bubble.create_interval, bubble.create_interval);
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

    private static View.OnClickListener obtainBubbleClickListener(final LetoActivity activity, FloatToolsConfig.Data.Bubble bubble) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view instanceof FloatBubbleView) {
                    FloatBubbleView bubbleView = (FloatBubbleView) view;
                    FloatViewManager.getInstance().removeBubbleView(activity, bubbleView.getBubbleId());
                    MGCDialogUtil.showMGCCoinDialog(activity, "", bubbleView.getCoinCount(), bubble.coins_multiple, CoinDialogScene.BUBBLE, null);
                }
            }
        };
    }
}
