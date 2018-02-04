package com.quantumsit.sportsinc.Aaa_looks;

/**
 * Created by Mona on 26-Dec-17.
 */

public class item_single_scores {

    String course_name, group_name, class_date, coach_name, coach_notes;
    int score, class_number , attend;

    public item_single_scores(String course_name, String group_name, String class_date, String coach_name, String coach_notes, int attend ,int score, int class_number) {
        this.course_name = course_name;
        this.group_name = group_name;
        this.class_date = class_date;
        this.coach_name = coach_name;
        this.coach_notes = coach_notes;
        this.attend = attend;
        this.score = score;
        this.class_number = class_number;
    }
}
