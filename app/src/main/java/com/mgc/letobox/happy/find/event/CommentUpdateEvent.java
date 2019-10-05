package com.mgc.letobox.happy.find.event;

//更新评论
public class CommentUpdateEvent {

    public CommentUpdateEvent(int id, int type){
        this.id = id;
        this.type = type;
    }

    public CommentUpdateEvent(int id, int type, int score){
        this.id = id;
        this.type = type;
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int  type;  //1：游戏评分 2：文章评论

    private int id;  //游戏id，or评论id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;
}
