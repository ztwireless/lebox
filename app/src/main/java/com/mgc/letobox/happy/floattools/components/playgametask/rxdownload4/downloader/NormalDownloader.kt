package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.downloader

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.Progress
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.TaskInfo
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.utils.*
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Flowable.generate
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody
import okio.*
import retrofit2.Response
import java.io.File
import java.util.concurrent.Callable

class NormalDownloader : Downloader {
    private var alreadyDownloaded = false

    private lateinit var file: File
    private lateinit var shadowFile: File
    private lateinit var sink:Sink
    private lateinit var bufferedSink:BufferedSink

    override fun download(taskInfo: TaskInfo, response: Response<ResponseBody>): Flowable<Progress> {
        val body = response.body() ?: throw RuntimeException("Response body is NULL")

        file = taskInfo.task.getFile()
        shadowFile = file.shadow()

        beforeDownload(taskInfo, response)
        sink = Okio.sink(file)
        bufferedSink = Okio.buffer(sink)

        return if (alreadyDownloaded) {
            Flowable.just(Progress(
                    downloadSize = response.contentLength(),
                    totalSize = response.contentLength()
            ))
        } else {
            startDownload(body, Progress(
                    totalSize = response.contentLength(),
                    isChunked = response.isChunked()
            ))
        }
    }

    private fun beforeDownload(taskInfo: TaskInfo, response: Response<ResponseBody>) {
        //make sure dir is exists
        val fileDir = taskInfo.task.getDir()
        if (!fileDir.exists() || !fileDir.isDirectory) {
            fileDir.mkdirs()
        }

        if (file.exists()) {
            if (taskInfo.validator.validate(file, response)) {
                alreadyDownloaded = true
            } else {
                file.delete()
                shadowFile.recreate()
            }
        } else {
            shadowFile.recreate()
        }
    }

    private fun startDownload(body: ResponseBody, progress: Progress): Flowable<Progress> {

        return generate(
                Callable {
                    InternalState(
                            body.source(),
                            bufferedSink
                    )
                },
                BiConsumer<InternalState, Emitter<Progress>> { internalState, emitter ->
                    internalState.apply {
                        val readLen = source.read(buffer, 8192L)

                        if (readLen == -1L) {
                            sink.flush()
                            shadowFile.renameTo(file)
                            emitter.onComplete()
                        } else {
                            sink.emit()
                            emitter.onNext(progress.apply {
                                downloadSize += readLen
                            })
                        }
                    }
                },
                Consumer {
                    it.apply {
                        sink.closeQuietly()
                        source.closeQuietly()
                    }
                })
    }

    class InternalState(
            val source: BufferedSource,
            val sink: BufferedSink,
            val buffer: Buffer = sink.buffer()
    )
}