package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

interface TaskRecorder {
    fun insert(task: Task)

    fun update(task: Task, status: Status)

    fun delete(task: Task)
}