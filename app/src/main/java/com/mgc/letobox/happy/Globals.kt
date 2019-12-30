package com.mgc.letobox.happy

import android.app.ActivityManager
import android.content.Context
import android.os.Process


fun toInt(text: String?): Int {
    return try {
        Integer.valueOf(text)
    } catch (e: Exception) {
        0
    }
}

fun killProcess(context: Context) {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val list: List<ActivityManager.RunningAppProcessInfo> = activityManager.runningAppProcesses
    for (runningAppProcessInfo in list) {
        if (runningAppProcessInfo.pid != Process.myPid()) {
            Process.killProcess(runningAppProcessInfo.pid)
        }
    }
    Process.killProcess(Process.myPid())
}