package com.mgc.letobox.happy.me.bean;

import com.mgc.leto.game.base.api.adext.FeedAd;

public class MeFeedAdModuleBean extends MeModuleBean {
	private FeedAd _feedAd;

	public MeFeedAdModuleBean(int type) {
		super(type);
	}

	public FeedAd getFeedAd() {
		return _feedAd;
	}

	public void setFeedAd(FeedAd _feedAd) {
		this._feedAd = _feedAd;
	}
}
