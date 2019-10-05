package com.mgc.letobox.happy.find.event;

public class FollowEvent {

    public FollowEvent(int uid, boolean isFollow){
        this.uid = uid;
        this.isFollow = isFollow;
    }

    private int  uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    private boolean isFollow;

}
