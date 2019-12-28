package com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.manager

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.flowables.ConnectableFlowable
import io.reactivex.rxkotlin.subscribeBy
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.Progress
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.delete
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.file
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.storage.Storage
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.task.Task
import com.mgc.letobox.happy.floattools.components.playgametask.rxdownload4.utils.safeDispose
import com.mgc.letobox.happy.floattools.components.playgametaskrxdownload4.manager.cancelNotification
import com.mgc.letobox.happy.floattools.components.playgametaskrxdownload4.manager.showNotification
import java.util.concurrent.TimeUnit.MILLISECONDS

class TaskManager(
        private val task: Task,
        private val storage: Storage,
        private val connectFlowable: ConnectableFlowable<Progress>,
        private val notificationCreator: NotificationCreator,
        taskRecorder: TaskRecorder,
        val taskLimitation: TaskLimitation
) {

    init {
        notificationCreator.init(task)
    }

    private val downloadHandler by lazy { StatusHandler(task, taskRecorder) }

    private val notificationHandler by lazy {
        StatusHandler(task, logTag = "Notification") {
            val notification = notificationCreator.create(task, it)
            showNotification(task, notification)
        }
    }

    //Download disposable
    private var disposable: Disposable? = null
    private var downloadDisposable: Disposable? = null
    private var notificationDisposable: Disposable? = null

    /**
     * Send Pending event by hand
     */
    internal fun sendPendingEventManual() {
        downloadHandler.onPending()
        notificationHandler.onPending()
    }

    /**
     * @param tag As the unique identifier for this subscription
     * @param receiveLastStatus If true, the last status will be received after subscribing
     */
    internal fun addCallback(tag: Any, receiveLastStatus: Boolean, callback: (Status) -> Unit) {
        downloadHandler.addCallback(tag, receiveLastStatus, callback)
    }

    internal fun removeCallback(tag: Any) {
        downloadHandler.removeCallback(tag)
    }

    internal fun currentStatus() = downloadHandler.currentStatus

    internal fun getFile() = task.file(storage)

    internal fun innerStart() {
        if (isStarted()) {
            return
        }

        subscribeNotification()

        subscribeDownload()

        disposable = connectFlowable.connect()
    }

    private fun subscribeDownload() {
        downloadDisposable = connectFlowable
                .doOnSubscribe { downloadHandler.onStarted() }
                .subscribeOn(mainThread())
                .observeOn(mainThread())
                .doOnNext { downloadHandler.onDownloading(it) }
                .doOnComplete { downloadHandler.onCompleted() }
                .doOnError { downloadHandler.onFailed(it) }
                .doOnCancel { downloadHandler.onPaused() }
                .subscribeBy()
    }

    private fun subscribeNotification() {
        notificationDisposable = connectFlowable.sample(500, MILLISECONDS)
                .doOnSubscribe { notificationHandler.onStarted() }
                .doOnNext { notificationHandler.onDownloading(it) }
                .doOnComplete { notificationHandler.onCompleted() }
                .doOnError { notificationHandler.onFailed(it) }
                .doOnCancel { notificationHandler.onPaused() }
                .subscribeBy()
    }

    internal fun innerStop() {
        notificationHandler.onPaused()
        downloadHandler.onPaused()

        notificationDisposable.safeDispose()
        downloadDisposable.safeDispose()
        disposable.safeDispose()
    }

    internal fun innerDelete() {
        innerStop()

        task.delete(storage)

        //special handle
        downloadHandler.onDeleted()

        cancelNotification(task)
    }

    private fun isStarted(): Boolean {
        return disposable != null && !disposable!!.isDisposed
    }
}