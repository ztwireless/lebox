package com.mgc.letobox.happy.circle.bean;

import com.mgc.letobox.happy.find.bean.BaseResultBean;

import java.io.Serializable;

/**
 * Created by DELL on 2018/7/31.
 */

public class CreateTieZiReponse extends BaseResultBean implements Serializable{


    /**
     * post_id : 163
     * symbol : TT
     * amount : 10
     */

    private int post_id;
    private String symbol;
    private float amount;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
