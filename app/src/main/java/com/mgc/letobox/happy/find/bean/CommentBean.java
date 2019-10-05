package com.mgc.letobox.happy.find.bean;

import java.io.Serializable;

/**
 * Create by zhaozhihui on 2018/9/7
 **/
public class CommentBean implements Serializable {

    /**
     * displayType : InlineImage
     * artwork : {"url":"http://download.mgc-games.com/picture/2/93465/5b90933b8ecb6.jpg"}
     */

    private String displayType;
    private ArtworkBean artwork;

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public ArtworkBean getArtwork() {
        return artwork;
    }

    public void setArtwork(ArtworkBean artwork) {
        this.artwork = artwork;
    }

    public static class ArtworkBean implements Serializable {
        /**
         * url : http://download.mgc-games.com/picture/2/93465/5b90933b8ecb6.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public String getEditorialCopy() {
        return editorialCopy;
    }

    public void setEditorialCopy(String editorialCopy) {
        this.editorialCopy = editorialCopy;
    }

    private String editorialCopy;
}
