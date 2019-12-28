package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import android.app.Notification
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

object EmptyNotification : NotificationCreator {
    override fun init(task: Task) {

    }

    override fun create(task: Task, status: Status): Notification? {
        return null
    }
}