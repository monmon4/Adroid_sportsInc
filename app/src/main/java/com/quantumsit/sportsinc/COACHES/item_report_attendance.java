package com.quantumsit.sportsinc.COACHES;

/**
 * Created by mona_ on 12/28/2017.
 */

public class item_report_attendance {

    String date, attended, course_name, class_number;

    public item_report_attendance(String date, String attended, String course_name, String class_number) {
        this.date = date;
        this.attended = attended;
        this.course_name = course_name;
        this.class_number = class_number;
    }
}
