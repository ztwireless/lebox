package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.storage

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

interface Storage {
    fun load(task: Task)

    fun save(task: Task)

    fun delete(task: Task)
}