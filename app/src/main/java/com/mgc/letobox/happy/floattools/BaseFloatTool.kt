package com.mgc.letobox.happy.floattools

import android.app.Activity
import android.graphics.Point
import android.util.Log
import com.leto.game.base.util.BaseAppUtil
import com.mgc.letobox.happy.util.LeBoxConstant
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference

abstract class BaseFloatTool(activity: Activity, val gameId: String) {
    val wrActivity: WeakReference<Activity> = WeakReference(activity)

    val TEST_ENV = BaseAppUtil.getMetaBooleanValue(activity, "MGC_TEST_ENV")

    private val TAG = BaseFloatTool::class.java.simpleName

    abstract fun isGameEnabled(): Boolean
    abstract fun init()
    abstract fun show(activity: Activity)
    open fun clean() {
        wrActivity.clear()
    }

    fun buildRetrofit(): Retrofit {
        Log.i(TAG, "buildRetrofit $TEST_ENV")
        return Builder()
                .baseUrl(if (TEST_ENV) LeBoxConstant.MGCServerUrlDev else LeBoxConstant.MGCServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun toInt(text: String?): Int {
        return try {
            Integer.valueOf(text)
        } catch (e: Exception) {
            0
        }
    }

    fun randomIn(min_coins: Int, max_coins: Int): Int {
        return (Math.random() * (max_coins - min_coins) + min_coins).toInt()
    }

    fun randomPointIn(activity: Activity, left: Float, top: Float, right: Float, bottom: Float): Point {
        val screenWidth = BaseAppUtil.getDeviceWidth(activity)
        val screenHeight = BaseAppUtil.getDeviceHeight(activity)
        val minX = (left * screenWidth).toInt()
        val maxX = screenWidth - (right * screenWidth).toInt()
        val minY = (top * screenHeight).toInt()
        val maxY = screenHeight - (bottom * screenHeight).toInt()
        return Point(randomIn(minX, maxX), randomIn(minY, maxY))
    }
}