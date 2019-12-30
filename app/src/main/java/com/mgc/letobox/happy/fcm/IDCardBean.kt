package com.mgc.letobox.happy.fcm

import android.support.annotation.Keep

//name: String, cardno: String, userToken: String, channelId: Int, mobile: String
@Keep
data class IDCardBean(
        val name: String = "",
        val cardno: String = "",
        val user_token: String = "",
        val channel_id: Int = 0,
        val mobile: String = ""
)