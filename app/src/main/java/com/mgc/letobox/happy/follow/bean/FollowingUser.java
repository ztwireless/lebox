package com.mgc.letobox.happy.follow.bean;

import android.support.annotation.Keep;

/**
 * Create by zhaozhihui on 2020-03-05
 **/

@Keep
public class FollowingUser {

    public String portrait;
    public String nickname;
    public float c_award;
    public FollowAwaken awaken;

    public static FollowingUser debugFake(int type){

        FollowingUser user = new FollowingUser();

        if(type==0){
            user.nickname = "w徒弟";
            user.c_award =100;
            user.portrait = "http://download.mgc-games.com/default/default.png";

            user.awaken = new FollowAwaken();
            user.awaken.status = 2;
            user.awaken.message = "今日未玩";

        }else{
            user.c_award = 10000;
            user.nickname = "t用户名称";
            user.portrait = "http://download.mgc-games.com/default/default.png";

            user.awaken = new FollowAwaken();
            user.awaken.status = 2;
            user.awaken.message = "今日未玩";
        }
        return user;
    }
}
