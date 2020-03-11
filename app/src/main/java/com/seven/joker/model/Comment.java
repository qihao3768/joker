package com.seven.joker.model;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class Comment extends BaseObservable implements Serializable {
    /**
     * id : 784
     * itemId : 6739143063064549000
     * commentId : 6739212214408380000
     * userId : 65200808093
     * commentType : 1
     * createTime : 1569095152
     * commentCount : 4454
     * likeCount : 152
     * commentText : 看见没。比甜蜜暴击好看一万倍！
     * imageUrl : null
     * videoUrl : null
     * width : 0
     * height : 0
     * hasLiked : false
     * author : {"id":978,"userId":65200808093,"name":"带鱼裹上面包糠","avatar":"","description":null,"likeCount":0,"topCommentCount":0,"followCount":0,"followerCount":0,"qqOpenId":null,"expires_time":0,"score":0,"historyCount":0,"commentCount":0,"favoriteCount":0,"feedCount":0,"hasFollow":false}
     * ugc : {"likeCount":153,"shareCount":0,"commentCount":4454,"hasFavorite":false,"hasLiked":true}
     */

    public int id;
    public long itemId;
    public long commentId;
    public long userId;
    public int commentType;
    public long createTime;
    public int commentCount;
    public int likeCount;
    public String commentText;
    public String imageUrl;
    public String videoUrl;
    public int width;
    public int height;
    public boolean hasLiked;
    public User author;
    public Ugc ugc;

    public Ugc getUgc() {
        if (ugc == null) {
            ugc = new Ugc();
        }
        return ugc;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Comment)) {
            return false;
        } else {
            Comment newComment = (Comment) obj;
            return id == newComment.id &&
                    itemId == newComment.itemId &&
                    commentId == newComment.commentId &&
                    userId == newComment.userId &&
                    commentType == newComment.commentType &&
                    createTime == newComment.createTime &&
                    commentCount == newComment.commentCount &&
                    likeCount == newComment.likeCount &&
                    TextUtils.equals(commentText, newComment.commentText) &&
                    TextUtils.equals(imageUrl, newComment.imageUrl) &&
                    TextUtils.equals(videoUrl, newComment.videoUrl) &&
                    width == newComment.width &&
                    height == newComment.height &&
                    hasLiked == newComment.hasLiked &&
                    (author != null && author.equals(newComment.author)) &&
                    (ugc != null && ugc.equals(newComment.ugc));
        }
    }
}
