package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BaseRequestBean;

@Keep
public class TaskListRequestBean extends BaseRequestBean {

	public int getTask_type() {
		return task_type;
	}

	public void setTask_type(int task_type) {
		this.task_type = task_type;
	}

	private int task_type;//1 新手任务


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	private  String mobile;
}
