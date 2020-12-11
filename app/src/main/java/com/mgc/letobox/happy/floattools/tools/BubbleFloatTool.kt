package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.graphics.Point
import android.util.Log
import android.view.View.OnClickListener
import com.mgc.leto.game.base.api.constant.Constant
import com.mgc.leto.game.base.api.mgc.RedPackRequest
import com.mgc.leto.game.base.interfaces.ILetoContainer
import com.mgc.leto.game.base.interfaces.ILetoGameContainer
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_bubble
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil
import com.mgc.leto.game.base.statistic.GameStatisticManager
import com.mgc.leto.game.base.statistic.StatisticEvent
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.view.FloatBubbleView
import java.util.*

class BubbleFloatTool(activity: Activity, gameId: String, val bubbleConfig: BenefitSettings_bubble) : BaseFloatTool(activity, gameId) {
    private val TAG = BubbleFloatTool::class.java.simpleName
    override fun isGameEnabled(): Boolean {
//        if (TEST_ENV) return true
        if (bubbleConfig != null && bubbleConfig.is_open == 1 && bubbleConfig.game_ids != null) {
            return bubbleConfig.game_ids.contains(gameId)
        }
        return false
    }

    private var bubbleTimer: Timer? = null

    override fun init() {
        Log.i(TAG, "init")
        if (bubbleConfig == null || bubbleConfig.create_interval <= 0 || wrActivity.get() == null) return
        val activity = wrActivity.get()!!

        val onBubbleClickListener: OnClickListener? = obtainBubbleClickListener(activity, bubbleConfig)

        bubbleTimer?.cancel()
        bubbleTimer?.purge()

        bubbleTimer = Timer()
        bubbleTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (activity.isFinishing) return
                val position: Point = randomPointIn(activity, bubbleConfig.left_upper, bubbleConfig.left_lower, bubbleConfig.right_upper, bubbleConfig.right_lower)
                val count = randomIn(bubbleConfig.min_coins, bubbleConfig.max_coins)
                activity?.runOnUiThread {
                    if (FloatViewManager.getInstance().bubbleCount < bubbleConfig.screen_max_times
                            && LeBoxSpUtil.todayBubbleTimes(gameId) < bubbleConfig.create_max_times) {
                        FloatViewManager.getInstance().addBubble(activity, count, position.x, position.y, onBubbleClickListener)
                        LeBoxSpUtil.bubbleOnce(gameId)
                    }
                }
            }
        }, bubbleConfig.create_interval * 1000.toLong(), bubbleConfig.create_interval * 1000.toLong())
    }

    override fun show(activity: Activity) {

    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removeAllBubbleViews(wrActivity.get())
        bubbleTimer?.cancel()
        bubbleTimer?.purge()
        bubbleTimer = null
    }

    private fun triggerJSBubbleAwardEvent(activity: Activity, awardId: String) {
        if (activity is ILetoGameContainer) {
            activity.notifyServiceSubscribeHandler("onAppBubbleAward", String.format("{\"award_id\": \"%s\"}", awardId), 0)
        }
    }

    private fun obtainBubbleClickListener(activity: Activity, bubble: BenefitSettings_bubble): OnClickListener {
        return OnClickListener { view ->
            if (view is FloatBubbleView) {

                //点击上报
                GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_BUBBLE, 0)

                FloatViewManager.getInstance().removeBubbleView(activity, view.bubbleId)

                // 根据open_ad_type不同
                if (bubbleConfig.open_ad_type == 1) {
                    val req = RedPackRequest()
                    req.mode = RedPackRequest.Mode.BUBBLE
                    req.redPackId = Int.MAX_VALUE
                    req.workflow = 1
                    req.scene = CoinDialogScene.BUBBLE
                    req.coin = view.coinCount
                    req.videoRatio = bubble.coins_multiple
                    MGCDialogUtil.showRedPackDialogForWorkflow1(activity, req)
                } else {
                    MGCDialogUtil.showMGCCoinDialog(activity, "", view.coinCount, bubble.coins_multiple, CoinDialogScene.BUBBLE, null)
                }
            }
        }
    }

}