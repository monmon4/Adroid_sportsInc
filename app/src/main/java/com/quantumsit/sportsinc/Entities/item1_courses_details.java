package com.quantumsit.sportsinc.Entities;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item1_courses_details {

    String class_name, start_date;
    int class_id;

    public item1_courses_details(String class_name, String start_date, int class_id) {
        this.class_name = class_name;
        this.start_date = start_date;
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public int getClass_id() {
        return class_id;
    }
}
