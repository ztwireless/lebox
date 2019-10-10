package com.mgc.letobox.happy.bean;

import android.support.annotation.Keep;

/**
 * Created by liu hong liang on 2016/11/12.
 */
@Keep
public class VersionResultBean {
    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    private String version_name; //版本名称
    private String  version;   //钱包版本号
    private  String content;  //更新内容
    private  int  type;  //是否需要强制更新 1是 2否

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getPackageurl() {
        return packageurl;
    }

    public void setPackageurl(String packageurl) {
        this.packageurl = packageurl;
    }

    private String packageurl;


}
