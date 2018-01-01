package com.quantumsit.sportsinc.COACHES.CurrentClassFragments;

import android.widget.EditText;

/**
 * Created by Mona on 31-Dec-17.
 */

public class item_score {

    String name;
    EditText score;

    public item_score(String name) {
        this.name = name;
    }

    public EditText getScore() {
        return score;
    }

    public void setScore(EditText score) {
        this.score = score;
    }
}
