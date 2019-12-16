package com.mgc.letobox.happy.event;

/**
 * Create by zhaozhihui on 2019-09-11
 **/
public class TabSwitchEvent {

    public int tabindex;

    // 如果tabindex是-1, 将使用id判断
    public int tabid;

    public TabSwitchEvent(int index){
        this.tabindex = index;
    }

    public TabSwitchEvent(int index, int id) {
        tabindex = index;
        tabid = id;
    }
}


