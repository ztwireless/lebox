package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.validator

import okhttp3.ResponseBody
import retrofit2.Response
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.utils.contentLength
import java.io.File

object SimpleValidator : Validator {
    override fun validate(file: File, response: Response<ResponseBody>): Boolean {
        return file.length() == response.contentLength()
    }
}