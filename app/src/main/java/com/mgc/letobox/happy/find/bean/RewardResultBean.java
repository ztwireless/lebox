package com.mgc.letobox.happy.find.bean;

import java.io.Serializable;

/**
 * Created by liu hong liang on 2016/11/11.
 */

public class RewardResultBean implements Serializable{

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    private float amount;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    private String symbol;


}
