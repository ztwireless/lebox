package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.downloader

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Response
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.Progress
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.TaskInfo

interface Downloader {
    fun download(taskInfo: TaskInfo, response: Response<ResponseBody>): Flowable<Progress>
}