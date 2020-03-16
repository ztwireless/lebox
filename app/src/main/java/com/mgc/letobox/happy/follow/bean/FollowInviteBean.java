package com.mgc.letobox.happy.follow.bean;

import android.support.annotation.Keep;

import java.util.List;

/**
 * Create by zhaozhihui on 2020-03-05
 **/

@Keep
public class FollowInviteBean {

    private String code ;
    private String dowanload_url ;

    public String getShareUrl() {
        return share_url;
    }

    public void setShareUrl(String share_url) {
        this.share_url = share_url;
    }

    private String share_url;

    private List<String> message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDownload_url() {
        return dowanload_url;
    }

    public void setDownload_url(String download_url) {
        this.dowanload_url = download_url;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<FollowRankUser> getRank_week() {
        return rank_week;
    }

    public void setRank_week(List<FollowRankUser> rank_week) {
        this.rank_week = rank_week;
    }

    public List<FollowRankUser> getRank_month() {
        return rank_month;
    }

    public void setRank_month(List<FollowRankUser> rank_month) {
        this.rank_month = rank_month;
    }

    private List<FollowRankUser> rank_week;
    private List<FollowRankUser> rank_month;



}
