package com.mgc.letobox.happy.model

open class BaseResponse<DATA>(
        val code: Int = 0,
        val msg: String = "",
        val data: DATA
) {
    fun isSuccess(): Boolean {
        return code == 200
    }
}