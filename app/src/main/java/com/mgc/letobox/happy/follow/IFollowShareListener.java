package com.mgc.letobox.happy.follow;

import com.leto.game.base.bean.SHARE_PLATFORM;

/**
 * Create by zhaozhihui on 2020-03-09
 **/
public interface IFollowShareListener {

    public void onShare();
    public void onShare(SHARE_PLATFORM platform);

}
