package com.quantumsit.sportsinc.Entities;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_about {

    int type;
    String title, content;

    public item_about(int type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
