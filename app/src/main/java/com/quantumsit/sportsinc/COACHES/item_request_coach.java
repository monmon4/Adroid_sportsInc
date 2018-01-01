package com.quantumsit.sportsinc.COACHES;

/**
 * Created by Mona on 28-Dec-17.
 */

public class item_request_coach {

    String creation_date, request_for, course_name_and_class_number, date, accepted;

    public item_request_coach(String creation_date, String request_for, String course_name_and_class_number, String date, String accepted) {
        this.creation_date = creation_date;
        this.request_for = request_for;
        this.course_name_and_class_number = course_name_and_class_number;
        this.date = date;
        this.accepted = accepted;
    }
}
