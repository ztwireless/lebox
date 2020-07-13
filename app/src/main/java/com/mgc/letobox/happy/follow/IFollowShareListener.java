package com.mgc.letobox.happy.follow;

import com.mgc.leto.game.base.bean.SHARE_PLATFORM;

/**
 * Create by zhaozhihui on 2020-03-09
 **/
public interface IFollowShareListener {
    void onShare();
    void onShare(SHARE_PLATFORM platform);
}
