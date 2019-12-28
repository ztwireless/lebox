package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

interface TaskLimitation {
    fun start(taskManager: TaskManager)

    fun stop(taskManager: TaskManager)

    fun delete(taskManager: TaskManager)
}