package com.seven.joker.model;


import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;

import java.io.Serializable;

public class Ugc extends BaseObservable implements Serializable {
    /**
     * likeCount : 153
     * shareCount : 0
     * commentCount : 4454
     * hasFavorite : false
     * hasLiked : true
     * hasdiss:false
     */

    public int likeCount;
    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasdiss;
    public boolean hasLiked;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof Ugc)) {
            return false;
        } else {
            Ugc newUgc = (Ugc) obj;
            return likeCount == newUgc.likeCount &&
                    shareCount == newUgc.shareCount &&
                    commentCount == newUgc.commentCount &&
                    hasFavorite == newUgc.hasFavorite &&
                    hasdiss == newUgc.hasdiss &&
                    hasLiked == newUgc.hasLiked;
        }
    }

}
