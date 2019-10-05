package com.mgc.letobox.happy.find.util;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.DefaultRetryPolicy;

/**
 * Created by zzh on 2018/4/3.
 */

public class RxVolleyUtil {

    public int initialTimeoutMs = 30000;
    public int maxNumRetries = 0;
    public int backOffMultiplier = 1;

    public RxVolleyUtil(){

    }

    public void setTimeout(int timeout){
        this.initialTimeoutMs = timeout;
    }

    public void setMaxNumRetries(int retries){
        this.maxNumRetries = retries;
    }

    public void setBackOffMultiplier(int backOffMultiplier){
        this.backOffMultiplier = backOffMultiplier;
    }

    public void  post(String url, HttpParams params, HttpCallback callback){
        (new RxVolley.Builder()).url(url).params(params).retryPolicy(new DefaultRetryPolicy(initialTimeoutMs,maxNumRetries,backOffMultiplier)).httpMethod(1).callback(callback).doTask();
    }
}
