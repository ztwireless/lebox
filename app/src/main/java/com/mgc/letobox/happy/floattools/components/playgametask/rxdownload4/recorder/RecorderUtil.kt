package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder

import android.arch.persistence.db.SupportSQLiteDatabase
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.Normal
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.Status
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder.StatusConverter.Companion.DOWNLOADING
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder.StatusConverter.Companion.PAUSED
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder.StatusConverter.Companion.STARTED
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task

internal const val DB_NAME = "TaskRecord.db"
internal const val TAB_NAME = "task_record"

internal fun fixAbnormalState(db: SupportSQLiteDatabase) {
    db.beginTransaction()
    try {
        db.execSQL("""UPDATE $TAB_NAME SET status = $PAUSED, abnormalExit = "1" WHERE status = $STARTED""")
        db.execSQL("""UPDATE $TAB_NAME SET status = $PAUSED, abnormalExit = "1" WHERE status = $DOWNLOADING""")
        db.setTransactionSuccessful()
    } finally {
        db.endTransaction()
    }
}

internal fun Task.map(status: Status = Normal()): TaskEntity {
    return TaskEntity(
            id = hashCode(),
            task = this,
            status = status,
            progress = status.progress
    )
}