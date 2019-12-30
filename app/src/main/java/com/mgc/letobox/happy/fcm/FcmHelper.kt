package com.mgc.letobox.happy.fcm

import android.os.CountDownTimer
import android.text.TextUtils
import com.ledong.lib.leto.main.LetoActivity
import com.leto.game.base.login.LoginManager
import com.mgc.letobox.happy.fcm.timer.FcmTryPlayCountDownTimer
import com.mgc.letobox.happy.fcm.timer.FcmYoungManCountDownTimer
import com.mgc.letobox.happy.model.FcmConfig
import com.mgc.letobox.happy.util.LeBoxSpUtil
import hugo.weaving.DebugLog

class FcmHelper {

    var playCountDownTimer: CountDownTimer? = null

    @DebugLog
    fun cancelPlayCountDownTimer() {
        playCountDownTimer?.cancel()
        playCountDownTimer = null
    }

    @DebugLog
    fun startFcmFunction(fcmConfig: FcmConfig?, idCard: String?, activity: LetoActivity) {
        if (fcmConfig == null) return

        cancelPlayCountDownTimer()
        if (!LoginManager.isSignedIn(activity)) {
            // 如果没登录
            //  试玩计时
            val restTime = FcmTryPlayCountDownTimer.TOTAL_TIME - LeBoxSpUtil.tryPlayTime()
            playCountDownTimer = FcmTryPlayCountDownTimer(activity, restTime, 5000)
            playCountDownTimer!!.start()
        } else if (ageOf(idCard) < 18) {
            // 如果是未成年
            //  未成年计时
            val restTime: Long = fcmConfig.obtainTodayMaxTime() - LeBoxSpUtil.todayPlayTime()
            playCountDownTimer = FcmYoungManCountDownTimer(activity, !TextUtils.isEmpty(idCard), restTime, 5000, fcmConfig.obtainStartTime(), fcmConfig.obtainEndTime())
            playCountDownTimer?.start()
        }
    }

    @DebugLog
    private fun ageOf(idCard: String?): Int {
        if (idCard == null) return 0
        if (idCard.length >= 14) {
            val date = idCard.substring(6, 14)
            return BirthdayToAgeUtil.ageOf(date)
        }
        return 0
    }
}