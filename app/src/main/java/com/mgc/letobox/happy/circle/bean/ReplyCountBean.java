package com.mgc.letobox.happy.circle.bean;

/**
 * Created by DELL on 2018/7/26.
 */

public class ReplyCountBean {
    private int postId;
    private int replyCount;

    public ReplyCountBean(int postId, int replyCount) {
        this.postId = postId;
        this.replyCount = replyCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
