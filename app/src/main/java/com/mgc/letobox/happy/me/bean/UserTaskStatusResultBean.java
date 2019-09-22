package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

/**
 * Create by zhaozhihui on 2019-09-11
 **/
@Keep
public class UserTaskStatusResultBean {

    public int getChannel_task_id() {
        return channel_task_id;
    }

    public void setChannel_task_id(int channel_task_id) {
        this.channel_task_id = channel_task_id;
    }

    private int channel_task_id;//任务Id

    private int task_status;//任务Id

    public int getTask_status() {
        return task_status;
    }

    public void setTask_status(int task_status) {
        this.task_status = task_status;
    }

    public int getTask_progress() {
        return task_progress;
    }

    public void setTask_progress(int task_progress) {
        this.task_progress = task_progress;
    }

    private int task_progress;//任务Id
}
