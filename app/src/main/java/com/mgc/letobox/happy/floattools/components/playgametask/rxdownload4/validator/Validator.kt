package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.validator

import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

interface Validator {
    fun validate(file: File, response: Response<ResponseBody>): Boolean
}