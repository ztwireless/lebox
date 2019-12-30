package com.mgc.letobox.happy.model

import android.support.annotation.Keep

@Keep
data class Certification(
        val is_bind_phone: Int = 0,
        val is_have_idcard: Int = 0,
        val idcard: String = ""
)