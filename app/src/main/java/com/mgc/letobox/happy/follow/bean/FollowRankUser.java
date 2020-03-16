package com.mgc.letobox.happy.follow.bean;

import android.support.annotation.Keep;

/**
 * Create by zhaozhihui on 2020-03-05
 **/

@Keep
public class FollowRankUser {

    public String portrait;
    public String username;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String nickname;
    public long invate_mem_count;
    public double invate_mem_award;


    public static FollowRankUser debugFake(int type){

        FollowRankUser user = new FollowRankUser();

        if(type==0){
            user.invate_mem_award = 1000;
            user.username = "w用户名称";
            user.invate_mem_count =100;
            user.portrait = "http://download.mgc-games.com/default/default.png";
        }else{
            user.invate_mem_award = 10000;
            user.username = "t用户名称";
            user.invate_mem_count =1000;
            user.portrait = "http://download.mgc-games.com/default/default.png";
        }
        return user;
    }
}
