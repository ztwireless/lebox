package com.mgc.letobox.happy.floattools.components

import android.app.Fragment
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.R.drawable
import com.mgc.letobox.happy.floattools.drawables.Model
import com.mgc.letobox.happy.floattools.drawables.Star
import com.mgc.letobox.happy.floattools.drawables.StarrySky
import com.mgc.letobox.happy.floattools.drawables.TitanAnimationDrawable
import com.mgc.letobox.happy.floattools.skymodels.AnimDrawableModel
import com.mgc.letobox.happy.floattools.skymodels.RedPacketModel
import com.mgc.letobox.happy.util.LeBoxSpUtil
import kotlinx.android.synthetic.main.fragment_redpacket_sea.*
import java.util.*

class RedPacketSeaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_redpacket_sea, container, false)
    }

    var totalTime: Int = 10000
    private lateinit var gameId: String
    private var coinCount: Int = 0
    private var coinMultiple: Int = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            gameId = it.getString(KEY_GAME_ID, "")
            coinCount = it.getInt(KEY_COIN_COUNT, 0)
            coinMultiple = it.getInt(KEY_COIN_MULTIPLE, 1)
        }
        redPacketDrawable = resources.getDrawable(drawable.redpacket)

        itemImage.setOnClickListener {
            itemImage.setOnClickListener(null)
            val animationDrawable = TitanAnimationDrawable(getResources().getDrawable(drawable.anim_redpacket_time_counter) as AnimationDrawable)
            animationDrawable.setAnimationListener {
                goGetRedPacket()
            }
            itemImage.setImageDrawable(animationDrawable)
            animationDrawable.start()
        }
    }

    private fun goGetRedPacket() {
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val sky = StarrySky(screenWidth, screenHeight)
        for (i in 0..20) {
            sky.addRandomStar()
        }
        sky.setOnModelOutListener {
            if (it is Star) {
                sky.addRandomStar()
                sky.removeModel(it)
            }
            if (it is RedPacketModel) {
                if (it.position.y > sky.height) {
                    sky.removeModel(it)
                }
            }
        }
        itemImage!!.setImageDrawable(sky)
        sky.start()
        addRedPacketPeriodly(sky, 300L)
        itemImage!!.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                sky.onTouch(event.x, event.y)
                true
            }
            false
        }
    }

    private fun addRedPacketPeriodly(sky: StarrySky, period: Long) {
        val timer = Timer()
        val startTime = System.currentTimeMillis()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    if (System.currentTimeMillis() - startTime <= totalTime) {
                        val packet = productRandomPacket()
                        sky.addModel(packet)
                        packet.onTouchListener = object : Model.OnTouchListener {
                            override fun onTouch(x: Float, y: Float) {
                                if (!activity.isFinishing) {
                                    // 添加爆炸model
                                    val animDrawable = TitanAnimationDrawable(resources.getDrawable(drawable.anim_bumb) as AnimationDrawable)
                                    val animDrawableModel = AnimDrawableModel(animDrawable, packet.position)
                                    sky.addModel(animDrawableModel)

                                    // 开始动画
                                    animDrawableModel.onAnimFinishedListener = {
                                        animDrawableModel.stop()
                                        sky.removeModel(animDrawableModel)
                                    }
                                    animDrawableModel.start()

                                    // 移除红包
                                    sky.removeModel(packet)
                                }
                            }
                        }
                    } else {
                        timer.cancel()
                        timeUp()
                    }
                }
            }
        }, 0, period)
    }

    private fun timeUp() {
        if (!activity.isFinishing) {
            MGCDialogUtil.showRedEnvelopesDialog(activity, coinCount, coinMultiple, CoinDialogScene.GIFT_RAIN) { p0, p1 ->
                Log.i(TAG, "gameId coinCount $gameId $coinCount")
                LeBoxSpUtil.hbrainOnce(gameId)
                activity.finish()
            }
        }
    }

    private lateinit var redPacketDrawable: Drawable
    private fun productRandomPacket(): RedPacketModel {
        val screenWidth = resources.displayMetrics.widthPixels
        val padding = screenWidth * 0.05f
        return RedPacketModel(redPacketDrawable,
                StarrySky.random(-50f, 50f),
                StarrySky.random(400f, 1000f),
                PointF(StarrySky.random(padding, screenWidth - padding - redPacketDrawable.intrinsicWidth),
                        -redPacketDrawable.intrinsicHeight.toFloat()))
    }

    companion object {
        private val TAG = RedPacketSeaFragment::class.java.simpleName
        const val KEY_GAME_ID = "game_id"
        const val KEY_COIN_COUNT = "coin_count"
        const val KEY_COIN_MULTIPLE = "coin_multiple"
    }
}