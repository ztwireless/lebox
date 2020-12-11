package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.mgc.leto.game.base.api.ApiContainer
import com.mgc.leto.game.base.api.constant.Constant
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_hbrain
import com.mgc.leto.game.base.statistic.GameStatisticManager
import com.mgc.leto.game.base.statistic.StatisticEvent
import com.mgc.leto.game.base.utils.ToastUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.floattools.components.RedPacketSeaActivity
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.view.FloatRedPacketSea
import java.util.*

class RedPacketSeaFloatTool(activity: Activity, gameId: String, val hbrainConfig: BenefitSettings_hbrain) : BaseFloatTool(activity, gameId) {
    private val TAG = RedPacketSeaFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
//        if (TEST_ENV) return true
        if (hbrainConfig != null && hbrainConfig.is_open == 1 && hbrainConfig.game_ids != null) {
            return hbrainConfig.game_ids.contains(gameId)
        }
        return false
    }

    override fun show(activity: Activity) {
        if (hbrainConfig != null && wrActivity.get() != null) {
            FloatViewManager.getInstance().showRedPacket(wrActivity.get(), hbrainConfig.default_x, hbrainConfig.default_y)
        }
    }

    private var timer: Timer? = null
    private val timerTask: TimerTask = object : TimerTask() {
        override fun run() {
            updateText()
        }
    }

    private fun updateText() {
        val lastTime = LeBoxSpUtil.hbrainLastTime(gameId)
        val restTime = hbrainConfig.cooling_time * 1000 - (System.currentTimeMillis() - lastTime)
        val playTimes = LeBoxSpUtil.todayHbrainTimes(gameId)
        val text = when {
            playTimes >= hbrainConfig.create_max_times -> wrActivity.get()?.resources?.getString(R.string.hbrain_rest_times_format, 0)
                    ?: ""
            restTime > 0 -> formatRestTime(restTime)
            else -> {
                // 剩余次数
                val restTimes = (hbrainConfig.create_max_times - playTimes).coerceAtLeast(0)

                wrActivity.get()?.resources?.getString(R.string.hbrain_rest_times_format, restTimes)
                        ?: ""
            }
        }
        wrActivity.get()?.runOnUiThread {
            FloatViewManager.getInstance().redPacketSeaView?.updateText(text)
        }
    }

    private fun formatRestTime(restTime: Long): String {
        val restSeconds = restTime / 1000
        val h = restSeconds / 3600
        val m = (restSeconds - h * 3600) / 60
        val s = restSeconds - h * 3600 - m * 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    override fun init() {
        Log.i(TAG, "init")
        if (wrActivity.get() == null) return

        val redPacketSea: FloatRedPacketSea = FloatViewManager.getInstance().getRedPacket(wrActivity.get(), hbrainConfig.default_x, hbrainConfig.default_y)
        // 更新view
        timer?.cancel()
        timer?.purge()
        timer = Timer()
        timer?.schedule(timerTask, 0, 1000)

        redPacketSea.setOnClickListener {
            if (wrActivity.get() == null) return@setOnClickListener
            val activity = wrActivity.get()

            //点击上报
            GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_GIFT_RAIN, 0)

            // 剩余次数
            val times = LeBoxSpUtil.todayHbrainTimes(gameId)
            if (times >= hbrainConfig.create_max_times) {
                ToastUtil.s(activity, R.string.hbrain_times_used_out)
                return@setOnClickListener
            }

            // 冷却时间
            val lastTime = LeBoxSpUtil.hbrainLastTime(gameId)
            if (System.currentTimeMillis() - lastTime < hbrainConfig.cooling_time * 1000) {
                ToastUtil.s(activity, R.string.hbrain_cooling)
                return@setOnClickListener
            }
            // 展示视频广告
            val apiContainer = ApiContainer(activity)
            apiContainer.showVideo(object : ApiContainer.IApiResultListener {
                override fun onApiSuccess(apiName: ApiContainer.ApiName?, o: Any?) {
                    if (wrActivity.get() == null) return

                    Log.i(TAG, "onApiSuccess")
                    val coinCount = randomIn(hbrainConfig.min_coins, hbrainConfig.max_coins)
                    RedPacketSeaActivity.start(wrActivity.get(), gameId, coinCount, hbrainConfig.coins_multiple)
                    apiContainer.destroy()
                }

                override fun onApiFailed(apiName: ApiContainer.ApiName?, o: Any?, b: Boolean) {
                    Log.i(TAG, "onApiFailed")
                    apiContainer.destroy()
                }
            })
        }
    }

    override fun clean() {
        super.clean()
        timer?.cancel()
        timer?.purge()
        timer = null
        FloatViewManager.getInstance().removeRedPacketView(wrActivity.get())

    }
}