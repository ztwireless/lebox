package com.mgc.letobox.happy.model

import com.google.gson.annotations.SerializedName

data class FcmConfig(
        val is_fcm: Int = 0,
        val message: Message,
        val anti: List<Anti>
) {
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

    data class Anti(
            val play_begin_time: String = "",
            val play_end_time: String = "",
            val holiday_max_time: String = "",
            val normal_max_time: String = "",
            val type: String = ""
    )
}