package com.quantumsit.sportsinc.Entities;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_name_id {

    int id; String name;
    boolean selected;

    public item_name_id(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}


