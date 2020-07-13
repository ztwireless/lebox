package com.mgc.letobox.happy.follow;

import com.mgc.leto.game.base.bean.SHARE_PLATFORM;

public interface ConfirmDialogListener {
	void setPlatform(SHARE_PLATFORM platform);

	void cancel();
}