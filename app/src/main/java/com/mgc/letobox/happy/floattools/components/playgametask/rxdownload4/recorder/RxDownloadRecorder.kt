package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.recorder

import android.annotation.SuppressLint
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager.*
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task
import io.reactivex.Flowable.fromIterable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers.io
import zlc.season.claritypotion.ClarityPotion.Companion.clarityPotion

@SuppressLint("CheckResult")
object RxDownloadRecorder {
    val taskDataBase by lazy { TaskDataBase.getInstance(clarityPotion) }

    fun getTask(url: String): Maybe<TaskEntity> {
        return getTask(Task(url))
    }

    fun getTask(task: Task): Maybe<TaskEntity> {
        return taskDataBase.taskDao().get(task.hashCode())
                .subscribeOn(io())
                .doOnSuccess {
                    it.status.progress = it.progress
                }
    }

    fun getTaskList(vararg url: String): Maybe<List<TaskEntity>> {
        val tasks = mutableListOf<Task>()
        url.mapTo(tasks) { Task(it) }
        return getTaskList(*tasks.toTypedArray())
    }

    fun getTaskList(vararg task: Task): Maybe<List<TaskEntity>> {
        val ids = mutableListOf<Int>()
        task.mapTo(ids) {
            it.hashCode()
        }
        return taskDataBase.taskDao().get(*ids.toIntArray())
                .subscribeOn(io())
                .doOnSuccess { mapResult(it) }
    }

    fun getTaskList(page: Int, pageSize: Int): Maybe<List<TaskEntity>> {
        return taskDataBase.taskDao().page(page * pageSize, pageSize)
                .subscribeOn(io())
                .doOnSuccess { mapResult(it) }
    }

    fun getTaskListWithStatus(page: Int, pageSize: Int, vararg status: Status): Maybe<List<TaskEntity>> {
        return taskDataBase.taskDao().pageWithStatus(page * pageSize, pageSize, *status)
                .subscribeOn(io())
                .doOnSuccess { mapResult(it) }
    }

    fun getAllTask(): Maybe<List<TaskEntity>> {
        return taskDataBase.taskDao().getAll()
                .subscribeOn(io())
                .doOnSuccess { mapResult(it) }
    }

    fun getAllTaskWithStatus(vararg status: Status): Maybe<List<TaskEntity>> {
        return taskDataBase.taskDao().getAllWithStatus(*status)
                .subscribeOn(io())
                .doOnSuccess { mapResult(it) }
    }

    fun startAll(callback: () -> Unit = {}) {
        getAllTaskWithStatus(Paused(), Failed())
                .flatMapPublisher { fromIterable(it) }
                .doOnNext {
                    it.task.createManager().start()
                }
                .observeOn(mainThread())
                .doOnComplete {
                    callback()
                }
                .subscribeBy()
    }


    fun stopAll(callback: () -> Unit = {}) {
        getAllTaskWithStatus(Pending(), Started(), Downloading())
                .flatMapPublisher { fromIterable(it) }
                .doOnNext {
                    it.task.createManager().stop()
                }
                .observeOn(mainThread())
                .doOnComplete {
                    callback()
                }
                .subscribeBy()
    }

    fun deleteAll(callback: () -> Unit = {}) {
        getAllTask()
                .flatMapPublisher { fromIterable(it) }
                .doOnNext {
                    it.task.createManager().delete()
                }
                .observeOn(mainThread())
                .doOnComplete {
                    callback()
                }
                .subscribeBy()
    }

    private fun mapResult(list: List<TaskEntity>) {
        list.forEach {
            it.status.progress = it.progress
        }
    }

    private fun Task.createManager(): TaskManager {
        return manager(
                recorder = RoomRecorder()
        )
    }
}