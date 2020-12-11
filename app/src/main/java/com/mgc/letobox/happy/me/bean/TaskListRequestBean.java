package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.mgc.leto.game.base.bean.BaseRequestBean;


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
	private  int api_v;    //该参数=1时,/channel/tasklist接口返回新版的每日任务,

	public int getApi_v() {
		return api_v;
	}

	public void setApi_v(int api_v) {
		this.api_v = api_v;
	}
}
