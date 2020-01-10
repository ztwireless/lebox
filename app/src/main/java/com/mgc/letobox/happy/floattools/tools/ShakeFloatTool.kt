package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.ledong.lib.leto.api.ApiContainer
import com.ledong.lib.leto.api.ApiContainer.ApiName
import com.ledong.lib.leto.api.ApiContainer.IApiResultListener
import com.ledong.lib.leto.api.constant.Constant
import com.ledong.lib.leto.interfaces.ILetoContainer
import com.ledong.lib.leto.mgc.bean.BenefitSettings_shake
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.leto.game.base.ad.AdPreloader
import com.leto.game.base.http.HttpCallbackDecode
import com.leto.game.base.statistic.GameStatisticManager
import com.leto.game.base.statistic.StatisticEvent
import com.leto.game.base.util.ToastUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.bean.ShakeResultBean
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.util.LeBoxUtil
import com.mgc.letobox.happy.view.ShakeShakeView
import java.io.IOException
import java.util.concurrent.Executors

class ShakeFloatTool(activity: Activity, gameId: String, val shakeConfig: BenefitSettings_shake) : BaseFloatTool(activity, gameId) {
    private val TAG = ShakeFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
        if (TEST_ENV) return true
        if (shakeConfig != null && shakeConfig.is_open == 1 && shakeConfig.game_ids != null) {
            return shakeConfig.game_ids.contains(gameId)
        }
        return false
    }

    override fun init() {
        Log.i(TAG, "init")
        if (wrActivity.get() != null) {
            initShakeView(wrActivity.get()!!)
        }
    }

    override fun show(activity: Activity) {
        val shake = shakeConfig
        if (shake != null && activity != null) {
            FloatViewManager.getInstance().showShakeShake(activity, shake.default_x, shake.default_y)
        }
    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removeShakeView(wrActivity.get())
    }

    private var lastShakeTime: Long = 0

    private fun initShakeView(activity: Activity) {
        val shake = shakeConfig
        val shakeView: ShakeShakeView = FloatViewManager.getInstance().initShakeShake(activity, shake.default_x, shake.default_y)
        shakeView.setOnClickListener {
            val todayTimes = LeBoxSpUtil.todayShakeTimes(gameId)
            val currentTime = System.currentTimeMillis()
//            Log.i(TAG, "lastShakeTime $lastShakeTime currentTime $currentTime" +
//                    " todayTimes $todayTimes max_times ${shake.max_times} isPreloaded ${AdPreloader.isInterstitialPreloaded()}")
            if (currentTime - lastShakeTime < 600) {
                // do nothing
            } else if (todayTimes >= shake.max_times) {
                lastShakeTime = System.currentTimeMillis()
                ToastUtil.s(activity, R.string.shake_time_used_out);
                //点击上报
                GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_SHAKE, 0)

            } else if (currentTime - lastShakeTime < 3000) {
                ToastUtil.s(activity, R.string.shake_nothing)
                //点击上报
                GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_SHAKE, 0)

            } else {
                lastShakeTime = System.currentTimeMillis()
                LeBoxSpUtil.shakeOnce(gameId)
                Executors.newSingleThreadExecutor().submit { shakeIt(activity) }

                //点击上报
                GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_SHAKE, 0)
            }
        }
    }

    private fun triggerJSShakeAwardEvent(activity: Activity, awardId: String) {
        if (activity is ILetoContainer) {
            activity.notifyServiceSubscribeHandler("onAppShakeAward", String.format("{\"award_id\": \"%s\"}", awardId), 0)
        }
    }

    private fun presentInterstitialAd(activity: Activity) {
        val apiContainer = ApiContainer(activity)
        apiContainer.presentInterstitialAd(object : IApiResultListener {
            override fun onApiSuccess(apiName: ApiName?, o: Any?) {
                Log.i(TAG, "onApiSuccess")
                apiContainer.destroy()
            }

            override fun onApiFailed(apiName: ApiName?, b: Boolean) {
                Log.i(TAG, "onApiFailed")
                apiContainer.destroy()
                ToastUtil.s(activity, R.string.obtain_ad_failed)
            }
        }, true)
    }

    private fun shakeIt(activity: Activity) {

        LeBoxUtil.getShakeResult(activity, gameId, object : HttpCallbackDecode<ShakeResultBean>(activity, null) {
            override fun onDataSuccess(shakeData: ShakeResultBean?) {
                activity.runOnUiThread {
                    try {
                        if (shakeData == null) {
                            if (!AdPreloader.isInterstitialPreloaded()) {
                                AdPreloader.preloadInterstitialIfNeeded()
                                ToastUtil.s(activity, R.string.shake_nothing)
                            } else {
                                presentInterstitialAd(activity)
                            }
                        } else {
                            when {
                                shakeData.add_coins_type == 1 && shakeData.add_coins > 0 ->
                                    MGCDialogUtil.showMGCCoinDialog(activity, "", shakeData.add_coins, shakeData.add_coins_multiple, CoinDialogScene.SHAKE) { b, i -> }
                                shakeData.add_coins_type == 2 && !TextUtils.isEmpty(shakeData.propid) ->
                                    triggerJSShakeAwardEvent(activity, shakeData.propid)
                                else ->
                                    if (!AdPreloader.isInterstitialPreloaded()) {
                                        AdPreloader.preloadInterstitialIfNeeded()
                                        ToastUtil.s(activity, R.string.shake_nothing)
                                    } else {
                                        presentInterstitialAd(activity)
                                    }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })

    }
}