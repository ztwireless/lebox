package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import com.leto.game.base.bean.BaseRequestBean;

@Keep
public class UserTaskStatusRequestBean extends BaseRequestBean {


	public int getChannel_task_id() {
		return channel_task_id;
	}

	public void setChannel_task_id(int channel_task_id) {
		this.channel_task_id = channel_task_id;
	}

	public long getProgress() {
		return progress;
	}

	public void setProgress(long progress) {
		this.progress = progress;
	}

	private int channel_task_id;//任务Id


	private long progress;  //任务进度


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	private  String mobile;
}
