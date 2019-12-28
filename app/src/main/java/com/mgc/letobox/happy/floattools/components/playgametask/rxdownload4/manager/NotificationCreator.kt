package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import android.app.Notification
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.Status
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

interface NotificationCreator {
    fun init(task: Task)

    fun create(task: Task, status: Status): Notification?
}