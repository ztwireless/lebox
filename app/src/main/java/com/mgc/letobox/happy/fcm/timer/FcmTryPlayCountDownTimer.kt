package com.mgc.letobox.happy.fcm.timer

import android.app.Activity
import android.os.CountDownTimer
import com.mgc.letobox.happy.fcm.AntiAddictionDialog
import com.mgc.letobox.happy.util.LeBoxSpUtil
import java.lang.ref.WeakReference

class FcmTryPlayCountDownTimer(activity: Activity, val millisInFuture: Long, val countDownInternal: Long) : CountDownTimer(millisInFuture, countDownInternal) {
    val wrActivity = WeakReference<Activity>(activity)

    companion object {
        const val TOTAL_TIME = 60 * 60 * 1000L
    }

    override fun onFinish() {
        LeBoxSpUtil.tryPlayFor(countDownInternal)
        if (wrActivity.get() != null) {
            AntiAddictionDialog.showPhone(wrActivity.get()!!.fragmentManager)
        }
    }

    override fun onTick(millisUntilFinished: Long) {
        LeBoxSpUtil.tryPlayFor(countDownInternal)
    }
}