package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.watcher

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

interface Watcher {
    fun watch(task: Task)

    fun unwatch(task: Task)
}