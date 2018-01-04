package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.widget.EditText;

/**
 * Created by Mona on 31-Dec-17.
 */

public class item_score {

    String name;
    String score;

    public item_score(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
