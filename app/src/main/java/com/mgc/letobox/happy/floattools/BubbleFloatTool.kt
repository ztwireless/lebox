package com.mgc.letobox.happy.floattools

import android.app.Activity
import android.graphics.Point
import android.util.Log
import android.view.View.OnClickListener
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.mgc.letobox.happy.model.FloatToolsConfig.Data.Bubble
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.view.FloatBubbleView
import java.util.*

class BubbleFloatTool(activity: Activity, gameId: String, val bubbleConfig: Bubble) : BaseFloatTool(activity, gameId) {
    private val TAG = BubbleFloatTool::class.java.simpleName
    override fun isGameEnabled(): Boolean {
        if (TEST_ENV) return true
        val gameIdInt = toInt(gameId)
        if (bubbleConfig.is_open == 1 && bubbleConfig.game_ids != null) {
            return bubbleConfig.game_ids.contains(gameIdInt)
        }
        return false
    }

    private var bubbleTimer: Timer? = null
    override fun init() {
        Log.i(TAG, "init")
        if (bubbleConfig.create_interval <= 0 || wrActivity.get() == null) return
        val activity = wrActivity.get()!!

        val onBubbleClickListener: OnClickListener? = obtainBubbleClickListener(activity, bubbleConfig)

        bubbleTimer?.cancel()

        bubbleTimer = Timer()
        bubbleTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (activity.isFinishing) return
                val position: Point = randomPointIn(activity, bubbleConfig.left_upper, bubbleConfig.left_lower, bubbleConfig.right_upper, bubbleConfig.right_lower)
                val count = randomIn(bubbleConfig.min_coins, bubbleConfig.max_coins)
                activity.runOnUiThread {
                    if (FloatViewManager.getInstance().bubbleCount < bubbleConfig.screen_max_times
                            && LeBoxSpUtil.todayBubbleTimes(gameId) < bubbleConfig.create_max_times) {
                        FloatViewManager.getInstance().addBubble(activity, count, position.x, position.y, onBubbleClickListener)
                        LeBoxSpUtil.bubbleOnce(gameId)
                    }
                }
            }
        }, bubbleConfig.create_interval * 1000.toLong(), bubbleConfig.create_interval * 1000.toLong())
    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removeAllBubbleViews(wrActivity.get())
        bubbleTimer?.cancel()
        bubbleTimer = null
    }

    private fun obtainBubbleClickListener(activity: Activity, bubble: Bubble): OnClickListener {
        return OnClickListener { view ->
            if (view is FloatBubbleView) {
                FloatViewManager.getInstance().removeBubbleView(activity, view.bubbleId)
                MGCDialogUtil.showMGCCoinDialog(activity, "", view.coinCount, bubble.coins_multiple, CoinDialogScene.BUBBLE, null)
            }
        }
    }

}