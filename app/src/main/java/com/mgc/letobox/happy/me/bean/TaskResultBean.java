package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
@Keep
public class TaskResultBean implements Serializable {

    public int getChannel_task_id() {
        return channel_task_id;
    }

    public void setChannel_task_id(int channel_task_id) {
        this.channel_task_id = channel_task_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public int getAward_coins() {
        return award_coins;
    }

    public void setAward_coins(int award_coins) {
        this.award_coins = award_coins;
    }

    public long getFinish_level() {
        return finish_level;
    }

    public void setFinish_level(int finish_level) {
        this.finish_level = finish_level;
    }

    public long getProcess() {
        return process;
    }

    public void setProcess(long process) {
        this.process = process;
    }

    public int getFinish_type() {
        return finish_type;
    }

    public void setFinish_type(int finish_type) {
        this.finish_type = finish_type;
    }


    int channel_task_id;

    String task_title;

    String task_desc;

    int award_coins;

    long finish_level;   //总进度

    long process;        //当前进度

    int finish_type;   // 完成任务类型 1等级 2时长 3称号 4数量 5加入微信朋友圈  6绑定手机号 7填写验证码

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    int classify;    //1 新手任务  2 日常任务

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;

}
