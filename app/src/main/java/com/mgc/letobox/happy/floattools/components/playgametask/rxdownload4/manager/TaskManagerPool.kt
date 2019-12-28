package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.download
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.downloader.Dispatcher
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.request.Request
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.storage.Storage
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.validator.Validator
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.watcher.Watcher

object TaskManagerPool {
    private val map = mutableMapOf<Task, TaskManager>()

    private fun add(task: Task, taskManager: TaskManager) {
        map[task] = taskManager
    }

    private fun get(task: Task): TaskManager? {
        return map[task]
    }

    private fun remove(task: Task) {
        map.remove(task)
    }

    fun obtain(
            task: Task,
            header: Map<String, String>,
            maxConCurrency: Int,
            rangeSize: Long,
            dispatcher: Dispatcher,
            validator: Validator,
            storage: Storage,
            request: Request,
            watcher: Watcher,
            notificationCreator: NotificationCreator,
            recorder: TaskRecorder,
            taskLimitation: TaskLimitation
    ): TaskManager {

        if (get(task) == null) {
            synchronized(this) {
                if (get(task) == null) {
                    val taskManager = task.createManager(
                            header = header,
                            maxConCurrency = maxConCurrency,
                            rangeSize = rangeSize,
                            dispatcher = dispatcher,
                            validator = validator,
                            storage = storage,
                            request = request,
                            watcher = watcher,
                            notificationCreator = notificationCreator,
                            recorder = recorder,
                            taskLimitation = taskLimitation
                    )
                    add(task, taskManager)
                }
            }
        }

        return get(task)!!
    }

    private fun Task.createManager(
            header: Map<String, String>,
            maxConCurrency: Int,
            rangeSize: Long,
            dispatcher: Dispatcher,
            validator: Validator,
            storage: Storage,
            request: Request,
            watcher: Watcher,
            notificationCreator: NotificationCreator,
            recorder: TaskRecorder,
            taskLimitation: TaskLimitation
    ): TaskManager {

        val download = download(
                header = header,
                maxConCurrency = maxConCurrency,
                rangeSize = rangeSize,
                dispatcher = dispatcher,
                validator = validator,
                storage = storage,
                request = request,
                watcher = watcher
        )
        return TaskManager(
                task = this,
                storage = storage,
                taskRecorder = recorder,
                connectFlowable = download.publish(),
                notificationCreator = notificationCreator,
                taskLimitation = taskLimitation
        )
    }

}