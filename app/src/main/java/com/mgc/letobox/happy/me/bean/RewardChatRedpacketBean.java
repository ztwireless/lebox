package com.mgc.letobox.happy.me.bean;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
@Keep
public class RewardChatRedpacketBean implements Serializable {

    public double amount;
    public long chatTime;
    public int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getChatTime() {
        return chatTime;
    }

    public void setChatTime(long chatTime) {
        this.chatTime = chatTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
