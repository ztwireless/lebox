package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.ledong.lib.leto.Leto;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.api.ApiContainer;
import com.mgc.leto.game.base.api.constant.Constant;
import com.mgc.leto.game.base.api.mgc.RedPackRequest;
import com.mgc.leto.game.base.interfaces.ILetoGameContainer;
import com.mgc.leto.game.base.listener.ILetoGameUpgradeListener;
import com.mgc.leto.game.base.listener.ILetoGiftRainListener;
import com.mgc.leto.game.base.listener.ILetoLifecycleListener;
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_hbrain;
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_upgrade;
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene;
import com.mgc.leto.game.base.mgc.dialog.IMGCCoinDialogListener;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.statistic.GameStatisticManager;
import com.mgc.leto.game.base.statistic.StatisticEvent;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
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
        LetoEvents.setGameUpgradeListener(new ILetoGameUpgradeListener() {

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
        LetoEvents.setGiftRainListener(new ILetoGiftRainListener() {
            @Override
            public void show(final Activity context, String gameId) {
                if (MGCSharedModel.benefitSettings != null && MGCSharedModel.benefitSettings.getHbrain() != null) {
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
                    apiContainer.setVideoScene(Constant.PLAY_VIDEO_SCENE_GIFT_RAIN_BEFORE);
                    apiContainer.showVideo(new ApiContainer.IApiResultListener() {
                        @Override
                        public void onApiSuccess(ApiContainer.ApiName n, Object data) {

                            int coinCount = (int) (Math.random() * (hbrainConfig.max_coins - hbrainConfig.min_coins) + hbrainConfig.min_coins);
                            RedPacketSeaActivity.start(context, gameId, coinCount, hbrainConfig.coins_multiple);
                            apiContainer.destroy();
                        }

                        @Override
                        public void onApiFailed(ApiContainer.ApiName n, Object data, boolean aborted) {
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
        LetoEvents.addLetoLifecycleListener(new ILetoLifecycleListener() {
            private List<BaseFloatTool> floatTools = new ArrayList<>();

            @Override
            public void onLetoAppLaunched(final ILetoGameContainer letoContainer, String gameId) {
                if(letoContainer.forbiddenFloatView()){
                    return;
                }
                Activity activity = (Activity) letoContainer.getLetoContext();
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
            public void onLetoAppLoaded(ILetoGameContainer letoContainer, String s) {
            }

            @Override
            public void onLetoAppShown(ILetoGameContainer letoContainer, String appId) {
            }

            @Override
            public void onLetoAppPaused(ILetoGameContainer letoContainer, String s) {
            }

            @Override
            public void onLetoAppResumed(ILetoGameContainer letoContainer, String s) {
            }

            @Override
            public void onLetoAppExit(ILetoGameContainer letoContainer, String s) {
                if(letoContainer.forbiddenFloatView()){
                    return;
                }
                Activity activity = (Activity) letoContainer.getLetoContext();
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
//        if (TEST_ENV) return true;
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
                    // 流程样式
                    // 1和2, 和主动显示红包api一样
                    // 3: 类似于样式1, 但是跳过出红包那一步, 直接放视频, 视频放完后出领取界面. 这个样式在主动
                    // 出红包的api里是不支持的
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
                        if (req.workflow < 1 || req.workflow > 3) {
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
                            case 2:
                                MGCDialogUtil.showRedPackDialogForWorkflow2(activity, req);
                                break;
                            default:
                                MGCDialogUtil.showRedPackDialogForWorkflow3(activity, req);
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