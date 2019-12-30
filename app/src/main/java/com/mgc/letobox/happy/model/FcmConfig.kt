package com.mgc.letobox.happy.model

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.mgc.letobox.happy.toInt
import hugo.weaving.DebugLog
import java.util.*

@Keep
data class FcmConfig(
        val is_fcm: Int = 0,
        val message: Message? = null,
        val anti: List<Anti>? = null
) {
    @DebugLog
    fun obtainMessageByType(type: String): String {
        message?.let {
            return when (type) {
                "-2" -> message.Neg2
                "-3" -> message.Neg3
                "-4" -> message.Neg4
                "-5" -> message.Neg5
                "-6" -> message.Neg6
                "experience" -> message.Neg3
                "play_time" -> message.Neg3
                "normal_time" -> message.Neg3
                "holiday_time" -> message.Neg3
                "login" -> message.Neg3
                "idcard" -> message.Neg3
                else -> ""
            }
        }
        return ""
    }

    @DebugLog
    fun obtainStartTime(): Int {
        val anti = obtainIdCardUserAnti()
        val beginTime = anti?.play_begin_time?.replace(":", "")
        return toInt(beginTime)
    }

    @DebugLog
    fun obtainEndTime(): Int {
        val anti = obtainIdCardUserAnti()
        val endTime = anti?.play_end_time?.replace(":", "")
        return toInt(endTime)
    }

    @DebugLog
    fun obtainTodayMaxTime(): Int {
        val anti = obtainIdCardUserAnti()
        return if (isHoliday()) toInt(anti?.holiday_max_time) else toInt(anti?.normal_max_time) * 1000
    }

    @DebugLog
    private fun isHoliday(): Boolean {
        val c = Calendar.getInstance();
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == 1 || dayOfWeek == 7
    }

    private fun obtainIdCardUserAnti(): Anti? {
        if (anti == null) return null
        anti.forEach {
            if (it.type == "2") return it
        }
        return null
    }

    @Keep
    data class Message(
            @SerializedName("-2")
            val Neg2: String = "",
            @SerializedName("-3")
            val Neg3: String = "",
            @SerializedName("-4")
            val Neg4: String = "",
            @SerializedName("-5")
            val Neg5: String = "",
            @SerializedName("-6")
            val Neg6: String = "",
            @SerializedName("experience")
            val experience: String = "",
            @SerializedName("play_time")
            val playTime: String = "",
            @SerializedName("normal_time")
            val normalTime: String = "",
            @SerializedName("holiday_time")
            val holidayTime: String = "",
            @SerializedName("login")
            val login: String = "",
            @SerializedName("idcard")
            val idcard: String = ""
    )

    @Keep
    data class Anti(
            val play_begin_time: String = "",
            val play_end_time: String = "",
            val holiday_max_time: String = "",
            val normal_max_time: String = "",
            val type: String = ""
    )
}