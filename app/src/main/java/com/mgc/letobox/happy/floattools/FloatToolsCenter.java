package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.api.constant.Constant;
import com.ledong.lib.leto.api.mgc.RedPackRequest;
import com.ledong.lib.leto.listener.ILetoGameUpgradeListener;
import com.ledong.lib.leto.listener.ILetoGiftRainListener;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.BenefitSettings_hbrain;
import com.ledong.lib.leto.mgc.bean.BenefitSettings_upgrade;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.statistic.GameStatisticManager;
import com.leto.game.base.statistic.StatisticEvent;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.floattools.components.RedPacketSeaActivity;
import com.mgc.letobox.happy.floattools.tools.BubbleFloatTool;
import com.mgc.letobox.happy.floattools.tools.PlayGameFloatTool;
import com.mgc.letobox.happy.floattools.tools.RedPacketSeaFloatTool;
import com.mgc.letobox.happy.floattools.tools.ShakeFloatTool;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxSpUtil;
import com.mgc.letobox.happy.view.UpgradeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private static Retrofit buildRetrofit() {
        Log.i(TAG, "buildRetrofit " + TEST_ENV);
        return new Retrofit.Builder()
                .baseUrl(TEST_ENV ? LeBoxConstant.MGCServerUrlDev : LeBoxConstant.MGCServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static void init(final Application app) {
        TEST_ENV = BaseAppUtil.getMetaBooleanValue(app, "MGC_TEST_ENV");
        addLetoLifecycleListener(app);
        setGameUpgradeListener(app);
        setGiftRainListener();
    }

    private static void setGameUpgradeListener(Application app) {
        Leto.getInstance().setGameUpgradeListener(new ILetoGameUpgradeListener() {

            @Override
            public void show(Activity context, String gameId, Map<String, Integer> gameInfo, JSONObject params) {
                LetoTrace.d(TAG, "upgrade show");
                if (isGameUpgradeEnabled(gameId)) {
                    initUpgradeView(context, gameId, params);

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

    private static void setGiftRainListener() {
        Leto.getInstance().setGiftRainListener(new ILetoGiftRainListener() {
            @Override
            public void show(final Activity context, String gameId) {
                if (MGCSharedModel.benefitSettings.getHbrain() != null) {
                    BenefitSettings_hbrain hbrainConfig = MGCSharedModel.benefitSettings.getHbrain();

                    // 剩余次数
                    int times = LeBoxSpUtil.todayHbrainTimes(gameId);
                    if (times >= hbrainConfig.create_max_times) {
                        ToastUtil.s(context, R.string.hbrain_times_used_out);
                        return;
                    }

                    // 冷却时间
                    long lastTime = LeBoxSpUtil.hbrainLastTime(gameId);
                    if (System.currentTimeMillis() - lastTime < hbrainConfig.cooling_time * 1000) {
                        ToastUtil.s(context, R.string.hbrain_cooling);
                        return;
                    }
                    // 展示视频广告
                    ApiContainer apiContainer = new ApiContainer(context);
                    apiContainer.showVideo(new ApiContainer.IApiResultListener() {
                        @Override
                        public void onApiSuccess(ApiContainer.ApiName n, Object data) {

                            int coinCount = (int) (Math.random() * (hbrainConfig.max_coins - hbrainConfig.min_coins) + hbrainConfig.min_coins);
                            RedPacketSeaActivity.start(context, gameId, coinCount, hbrainConfig.coins_multiple);
                            apiContainer.destroy();
                        }

                        @Override
                        public void onApiFailed(ApiContainer.ApiName n, boolean aborted) {
                            Log.i(TAG, "onApiFailed");
                            apiContainer.destroy();
                        }
                    });

                }
            }

            @Override
            public void hide(Activity context, String s) {

            }
        });
    }

    private static void addLetoLifecycleListener(Application app) {
        Leto.getInstance().addLetoLifecycleListener(new ILetoLifecycleListener() {
            private List<BaseFloatTool> floatTools = new ArrayList<>();

            @Override
            public void onLetoAppLaunched(final LetoActivity activity, String gameId) {
                if (MGCSharedModel.benefitSettings != null) {
                    if (MGCSharedModel.benefitSettings.getBubble() != null) {
                        BubbleFloatTool bubbleFloatTool = new BubbleFloatTool(activity, gameId, MGCSharedModel.benefitSettings.getBubble());
                        floatTools.add(bubbleFloatTool);
                        if (bubbleFloatTool.isGameEnabled()) {
                            bubbleFloatTool.init();
//                            if(MGCSharedModel.benefitSettings.getBubble().is_game_show ==1){
//                                bubbleFloatTool.show(activity);
//                            }
                        }
                    }
                    if (MGCSharedModel.benefitSettings.getShake() != null) {
                        ShakeFloatTool shakeFloatTool = new ShakeFloatTool(activity, gameId, MGCSharedModel.benefitSettings.getShake());
                        floatTools.add(shakeFloatTool);
                        if (shakeFloatTool.isGameEnabled()) {
                            shakeFloatTool.init();
                            if (MGCSharedModel.benefitSettings.getShake().is_game_show == 1
                                    && MGCSharedModel.benefitSettings.getShake().is_game_control != 1) {
                                shakeFloatTool.show(activity);
                            }
                        }
                    }
                    if (MGCSharedModel.benefitSettings.getHbrain() != null) {
                        RedPacketSeaFloatTool redPacketSeaFloatTool = new RedPacketSeaFloatTool(activity, gameId, MGCSharedModel.benefitSettings.getHbrain());
                        floatTools.add(redPacketSeaFloatTool);
                        if (redPacketSeaFloatTool.isGameEnabled()) {
                            redPacketSeaFloatTool.init();
                            if (MGCSharedModel.benefitSettings.getHbrain().is_game_show == 1
                                    && MGCSharedModel.benefitSettings.getHbrain().is_game_control != 1) {
                                redPacketSeaFloatTool.show(activity);
                            }
                        }
                    }
                    if (MGCSharedModel.benefitSettings.getPlaygametask() != null) {
                        PlayGameFloatTool playGameFloatTool = new PlayGameFloatTool(activity, gameId, MGCSharedModel.benefitSettings.getPlaygametask());
                        floatTools.add(new PlayGameFloatTool(activity, gameId, MGCSharedModel.benefitSettings.getPlaygametask()));
                        if (playGameFloatTool.isGameEnabled()) {
                            playGameFloatTool.init();
                            if (MGCSharedModel.benefitSettings.getPlaygametask().is_game_show == 1
                                    && MGCSharedModel.benefitSettings.getPlaygametask().is_game_control != 1
                            ) {
                                playGameFloatTool.show(activity);
                            }
                        }
                    }
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
                FloatViewManager.getInstance().removeUpgradeView(activity);

                Iterator<BaseFloatTool> it = floatTools.iterator();
                while (it.hasNext()) {
                    it.next().clean();
                    it.remove();
                }
            }
        });
    }
//

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

    private static void initUpgradeView(Activity activity, String gameId, JSONObject params) {
        if (MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getUpgrade() != null && activity != null) {
            final BenefitSettings_upgrade update = MGCSharedModel.benefitSettings.getUpgrade();

            // 如果show里面指定了位置, 使用show的位置
            int xDirection = update.getDefault_x();
            float yRatio = update.getDefault_y();
            if (params != null) {
                String gravity = params.optString("gravity", "unspecified");
                if (!gravity.equals("unspecified")) {
                    xDirection = gravity.equals("left") ? 0 : 1;
                }
                if (params.has("percent_v")) {
                    yRatio = (float) params.optDouble("percent_v", update.getDefault_y());
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

                    // create request
                    RedPackRequest req = new RedPackRequest();
                    req.mode = RedPackRequest.Mode.UPGRADE_REMOTE;
                    req.levelReward = upgradeView.getRewardLevel();
                    req.upgrade = update;
                    req.redPackId = -1;
                    req.scene = CoinDialogScene.GAME_UPGRADE;
                    if (req.levelReward != null) {
                        req.coin = req.levelReward.getCoins();
                        req.videoRatio = req.upgrade.getCoins_multiple();
                        req.workflow = MGCSharedModel.upgradeRedPackStyle;
                        if(req.workflow < 1 || req.workflow > 2) {
                            req.workflow = 1;
                        }
                        req.listener = new IMGCCoinDialogListener() {
                            @Override
                            public void onExit(boolean video, int coinGot) {
                                if (coinGot > 0 && upgradeView != null) {
                                    upgradeView.resetRewardStatus(req.levelReward.level_list_id);
                                    upgradeView.getGameUpgradeSetting(activity, gameId);
                                }
                            }
                        };
                        switch (req.workflow) {
                            case 1:
                                MGCDialogUtil.showRedPackDialogForWorkflow1(activity, req);
                                break;
                            default:
                                MGCDialogUtil.showRedPackDialogForWorkflow2(activity, req);
                                break;
                        }
                    } else {
                        ToastUtil.s(activity, "请升级后再试");
                    }
                    return true;
                }
            });
        }
    }
}