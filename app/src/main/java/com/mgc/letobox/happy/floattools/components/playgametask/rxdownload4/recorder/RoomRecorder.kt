package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder

import android.annotation.SuppressLint
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.Status
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.TaskRecorder
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder.RxDownloadRecorder.taskDataBase
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task
import zlc.season.ironbranch.ioThread

@SuppressLint("CheckResult")
class RoomRecorder : TaskRecorder {
    override fun insert(task: Task) {
        ioThread {
            taskDataBase.taskDao().insert(task.map())
        }
    }

    override fun update(task: Task, status: Status) {
        ioThread {
            taskDataBase.taskDao().update(task.map(status))
        }
    }

    override fun delete(task: Task) {
        ioThread {
            taskDataBase.taskDao().delete(task.map())
        }
    }
}