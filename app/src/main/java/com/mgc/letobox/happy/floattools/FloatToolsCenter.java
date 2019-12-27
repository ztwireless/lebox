package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.LetoConst;
import com.ledong.lib.leto.listener.ILetoGameUpgradeListener;
import com.ledong.lib.leto.listener.ILetoLifecycleListener;
import com.ledong.lib.leto.main.LetoActivity;
import com.ledong.lib.leto.mgc.bean.CoinDialogScene;
import com.ledong.lib.leto.mgc.bean.GameLevelResultBean;
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.floattools.tools.BubbleFloatTool;
import com.mgc.letobox.happy.floattools.tools.RedPacketSeaFloatTool;
import com.mgc.letobox.happy.floattools.tools.ShakeFloatTool;
import com.mgc.letobox.happy.model.DataCenter;
import com.mgc.letobox.happy.model.FloatToolsConfig;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.view.UpgradeView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        loadConfig(app);
        addLetoLifecycleListener(app);
        setGameUpgradeListener(app);
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataCenter.Companion.obtainFcmConfig(toInt(BaseAppUtil.getChannelID(app)), LetoConst.SDK_OPEN_TOKEN);
                DataCenter.Companion.requestCertification("13009944290", LetoConst.SDK_OPEN_TOKEN);
                DataCenter.Companion.requestIdCard("防静电", "110101199003077352");
            }
        }).start();
*/
    }

    private static void setGameUpgradeListener(Application app) {
        Leto.getInstance().setGameUpgradeListener(new ILetoGameUpgradeListener() {
            @Override
            public void show(Activity activity, String gameId, String gameInfo) {
                LetoTrace.d(TAG, "upgrade show");
                if (isGameUpgradeEnabled(gameId)) {
                    initUpgradeView(activity, gameId);
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

    private static void addLetoLifecycleListener(Application app) {
        Leto.getInstance().addLetoLifecycleListener(new ILetoLifecycleListener() {
            private List<BaseFloatTool> floatTools = new ArrayList<>();

            @Override
            public void onLetoAppLaunched(final LetoActivity activity, String gameId) {
                if (floatToolsConfig != null && floatToolsConfig.getData() != null) {
                    if (floatToolsConfig.getData().getBubble() != null) {
                        floatTools.add(new BubbleFloatTool(activity, gameId, floatToolsConfig.getData().getBubble()));
                    }
                    if (floatToolsConfig.getData().getShake() != null) {
                        floatTools.add(new ShakeFloatTool(activity, gameId, floatToolsConfig.getData().getShake()));
                    }
                    if (floatToolsConfig.getData().getHbrain() != null) {
                        floatTools.add(new RedPacketSeaFloatTool(activity, gameId, floatToolsConfig.getData().getHbrain()));
                    }
                }
                for (BaseFloatTool floatTool : floatTools) {
                    if (floatTool.isGameEnabled()) {
                        floatTool.init();
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

    private static void loadConfig(Application app) {
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
                        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getHbrain() != null) {
                            floatToolsConfig.getData().getHbrain().cooling_time = 120;
                            floatToolsConfig.getData().getHbrain().create_max_times = 1;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    private static void initUpgradeView(Activity activity, String gameId) {
        if (floatToolsConfig != null && floatToolsConfig.getData() != null && floatToolsConfig.getData().getUpgrade() != null) {
            final FloatToolsConfig.Data.Upgrade update = floatToolsConfig.getData().getUpgrade();
            UpgradeView upgradeView = FloatViewManager.getInstance().showUpgradeView(activity, gameId, update.default_x, update.default_y);
//            UpgradeView upgradeView = FloatViewManager.getInstance().showUpgradeView(activity, gameId, 1, 0.5f);
            upgradeView.setGameId(gameId);
            upgradeView.getGameUpgradeSetting(activity, gameId);
            upgradeView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {
                    LetoTrace.d("UpgradeView", "click me......");
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