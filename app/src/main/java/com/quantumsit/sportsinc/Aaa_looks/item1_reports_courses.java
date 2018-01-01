package com.quantumsit.sportsinc.Aaa_looks;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item1_reports_courses {

    String date, class_number, attendance, score, coach_note;

    public item1_reports_courses(String date) {
        this.date = date;
    }

    public item1_reports_courses(String class_number, String attendance, String score, String coach_note) {
        this.attendance = attendance;
        this.score = score;
        this.coach_note = coach_note;
        this.class_number = class_number;

    }
}
