package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.DEFAULT_SAVE_PATH
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.Progress
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.downloader.Dispatcher
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.request.Request
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.storage.Storage
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.utils.fileName
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.validator.Validator
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.watcher.Watcher
import io.reactivex.Flowable

class TaskInfo(
        val task: Task,
        val header: Map<String, String>,
        val maxConCurrency: Int,
        val rangeSize: Long,
        val dispatcher: Dispatcher,
        val validator: Validator,
        val storage: Storage,
        val request: Request,
        val watcher: Watcher
) {
    fun start(): Flowable<Progress> {
        //Before start download, we should load task first.
        storage.load(task)

        //Identify if the task is being watched.
        var watchFlag = false

        return request.get(task.url, header)
                .doOnNext {
                    check(it.isSuccessful) { "Request failed!" }

                    if (task.saveName.isEmpty()) {
                        task.saveName = it.fileName()
                    }
                    if (task.savePath.isEmpty()) {
                        task.savePath = DEFAULT_SAVE_PATH
                    }

                    try {
                        //Watch task, should be done when the task
                        //has save path and save name.
                        watcher.watch(task)
                        watchFlag = true
                    } catch (e: Throwable) {
                        throw e
                    }

                    //save task info
                    storage.save(task)
                }
                .flatMap {
                    dispatcher.dispatch(it).download(this, it)
                }
                .doFinally {
                    //unwatch task
                    if (watchFlag) {
                        watcher.unwatch(task)
                    }
                }
    }
}