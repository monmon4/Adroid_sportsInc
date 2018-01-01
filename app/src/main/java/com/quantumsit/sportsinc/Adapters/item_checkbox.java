package com.quantumsit.sportsinc.Adapters;

/**
 * Created by mona_ on 12/30/2017.
 */

public class item_checkbox {

    String name;
    boolean selected;

    public item_checkbox(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {return name;}

    public boolean getSelected() {
        return selected;
    }
}
