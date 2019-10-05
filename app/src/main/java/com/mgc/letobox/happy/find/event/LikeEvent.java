package com.mgc.letobox.happy.find.event;

public class LikeEvent {

    public LikeEvent(int id, int isLike){
        this.id = id;
        this.isLike = isLike;
    }

    private int  id;

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
    }


    public int isLike() {
        return isLike;
    }

    public void setLike(int like) {
        isLike = like;
    }

    private int isLike;

}
