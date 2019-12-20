package com.mgc.letobox.happy.floattools

import android.app.Fragment
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.dialog.IMGCCoinDialogListener
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.R.drawable
import kotlinx.android.synthetic.main.fragment_redpacket_sea.*
import java.util.*

class RedPacketSeaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_redpacket_sea, container, false)
    }

    var totalTime: Int = 10000
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        redPacketDrawable = resources.getDrawable(drawable.redpacket)

        itemImage.setOnClickListener {
            itemImage.setOnClickListener(null)
            val animationDrawable = TitanAnimationDrawable(getResources().getDrawable(R.drawable.anim_redpacket_time_counter) as AnimationDrawable)
            animationDrawable.setAnimationListener {
                goGetedPacket()
            }
            itemImage.setImageDrawable(animationDrawable)
            animationDrawable.start()
        }
    }

    private fun goGetedPacket() {
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
//                                    val drawable1 = resources.getDrawable(drawable.anim_bumb) as AnimationDrawable
                                    val animDrawable = TitanAnimationDrawable(resources.getDrawable(drawable.anim_bumb) as AnimationDrawable)
                                    val animDrawableModel = AnimDrawableModel(animDrawable, packet.position)
                                    sky.addModel(animDrawableModel)

                                    // 开始动画
                                    animDrawableModel.onAnimFinishedListener = {
                                        animDrawableModel.stop()
                                        sky.removeModel(animDrawableModel)
                                    }
//                                    animDrawable.start()
//                                    drawable1.start()
                                    animDrawableModel.start()

                                    // 移除红堡
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
            MGCDialogUtil.showRedEnvelopesDialog(activity, 5, CoinDialogScene.GIFT_RAIN) { p0, p1 ->
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
    }
}