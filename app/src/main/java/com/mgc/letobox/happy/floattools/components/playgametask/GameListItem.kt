package com.mgc.letobox.happy.floattools.components.playgametask

import android.content.Context
import android.util.Log
import com.bytedance.bdtracker.it
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.*
import com.mgc.letobox.happy.floattools.components.playgametask.utils.DownloadProgressButton
import com.mgc.letobox.happy.floattools.components.playgametask.utils.ProgressButton
import com.mgc.letobox.happy.floattools.components.playgametask.utils.createTaskManager
import com.mgc.letobox.happy.floattools.components.playgametask.utils.installApk
import zlc.season.yasha.YashaItem

class GameListItem(
        val name: String,
        val icon: String,
        val desc: String,
        val packName: String,
        val url: String
) : YashaItem {

    private var tag: Any? = null

    fun action(context: Context) {
        val taskManager = url.createTaskManager()
        when (taskManager.currentStatus()) {
            is Normal -> taskManager.start()
            is Pending -> taskManager.stop()
            is Started -> taskManager.stop()
            is Downloading -> taskManager.stop()
            is Failed -> taskManager.start()
            is Paused -> taskManager.start()
            is Completed -> context.installApk(taskManager.file())
            is Deleted -> taskManager.start()
        }
    }

    fun subscribe(btn_action: ProgressButton, context: Context) {
        val taskManager = url.createTaskManager()

        val currentStatus = taskManager.currentStatus()
        btn_action.setStatus(currentStatus)
        btn_action.text = stateStr(context, currentStatus)

        tag = taskManager.subscribe {
            Log.d("dongxt","status ="+ it)
//            if(it is Completed){
//                context.installApk(taskManager.file())
//            }
            btn_action.setStatus(currentStatus)
            btn_action.text = stateStr(context, currentStatus)
        }
    }

    private fun stateStr(context: Context, status: Status): String {
        return when (status) {
            is Normal -> context.getString(R.string.start_text)
            is Pending -> context.getString(R.string.pending_text)
            is Started -> context.getString(R.string.pause_text)
            is Downloading -> context.getString(R.string.pause_text)
            is Paused -> context.getString(R.string.continue_text)
            is Completed -> context.getString(R.string.install_text)
            is Failed -> context.getString(R.string.retry_text)
            is Deleted -> context.getString(R.string.start_text)
            else -> ""
        }
    }

    fun dispose() {
        tag?.let {
            url.createTaskManager().dispose(it)
        }
    }

    override fun cleanUp() {
        dispose()
    }
}