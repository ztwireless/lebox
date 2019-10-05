package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

public class GameDetailRequestBean extends BaseRequestBean {
	private String gameid;

	public GameDetailRequestBean() {
		setFrom("6");
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
}
