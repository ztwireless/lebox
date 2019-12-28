package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

object EmptyRecorder : TaskRecorder {

    override fun insert(task: Task) {
    }

    override fun update(task: Task, status: Status) {
    }

    override fun delete(task: Task) {
    }
}