package com.quantumsit.sportsinc.Aaa_looks;

/**
 * Created by Mona on 26-Dec-17.
 */

public class item_single_reports_courses {

    String course_name, group_name;
    int course_id, group_id, score;
    double attendance;
    int trainee_id;
    String trainee_name;

    public item_single_reports_courses(String course_name, String group_name, int course_id, int group_id, double attendance, int score) {
        this.course_name = course_name;
        this.group_name = group_name;
        this.course_id = course_id;
        this.group_id = group_id;
        this.attendance = attendance;
        this.score = score;
    }

    public item_single_reports_courses(int trainee_id, String trainee_name ,String course_name, String group_name, int course_id, int group_id, double attendance, int score) {
        this.course_name = course_name;
        this.group_name = group_name;
        this.course_id = course_id;
        this.group_id = group_id;
        this.score = score;
        this.attendance = attendance;
        this.trainee_id = trainee_id;
        this.trainee_name = trainee_name;
    }
}
