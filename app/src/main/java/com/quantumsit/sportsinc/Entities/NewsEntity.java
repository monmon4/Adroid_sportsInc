package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;

/**
 * Created by Bassam on 1/3/2018.
 */

public class NewsEntity implements Serializable {
    String content ,imageurl;

    public NewsEntity() {
    }

    public NewsEntity(String content, String imageurl) {
        this.content = content;
        this.imageurl = imageurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
