package com.mgc.letobox.happy.fcm.timer

import android.app.Activity
import android.os.CountDownTimer
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.fcm.AntiAddictionDialog
import com.mgc.letobox.happy.toInt
import com.mgc.letobox.happy.util.LeBoxSpUtil
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

class FcmYoungManCountDownTimer(activity: Activity, val bindIdCard: Boolean, val millisInFuture: Long, val countDownInternal: Long, val startTime: Int, val endTime: Int) : CountDownTimer(millisInFuture, countDownInternal) {
    val wrActivity = WeakReference<Activity>(activity)
    override fun onFinish() {
        if (wrActivity.get() == null) return
        if (!bindIdCard) {
            AntiAddictionDialog.showPhone(wrActivity.get()!!.fragmentManager)
        } else {
            val content = getString(if (isHoliday()) R.string.fcm_content_holiday else R.string.fcm_content_workday)
            AntiAddictionDialog.showContent(wrActivity.get()!!.fragmentManager, getString(R.string.warm_tip), content, getString(R.string.ok_to_exit))
        }
    }

    override fun onTick(millisUntilFinished: Long) {
        LeBoxSpUtil.todayPlayFor(countDownInternal)
        val time = currentTime()
        if (time > endTime || time < startTime) {
            if (!bindIdCard) {
                AntiAddictionDialog.showPhone(wrActivity.get()!!.fragmentManager)
            } else {
                AntiAddictionDialog.showContent(wrActivity.get()!!.fragmentManager, getString(R.string.warm_tip), getString(R.string.fcm_content_time), getString(R.string.ok_to_exit))
            }
        }
    }

    val timeFormatter = SimpleDateFormat("HHmmss")
    private fun currentTime(): Int {
        val time = timeFormatter.format(Date())
        return toInt(time)
    }

    fun getString(resId: Int): String {
        if (wrActivity.get() != null) {
            return wrActivity.get()!!.getString(resId)
        }
        return ""
    }

    private fun isHoliday(): Boolean {
        val c = Calendar.getInstance();
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == 1 || dayOfWeek == 7
    }
}