package com.quantumsit.sportsinc.COACHES.ReportsFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Mona on 01-Jan-18.
 */

public class item_reports_finished_courses implements Serializable {

    String course_name, group_name , pool_name;

    int course_id , group_id ;

    public item_reports_finished_courses(String course_name, String group_number) {
        this.course_name = course_name;
        this.group_name = group_number;
    }

    public item_reports_finished_courses(JSONObject jsonObject) {
        try {
            course_name = jsonObject.getString("course_name");
            course_id = jsonObject.getInt("course_ID");
            group_name = jsonObject.getString("group_name");
            group_id = jsonObject.getInt("groups_ID");
            pool_name = jsonObject.getString("pool_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getPool_name() {
        return pool_name;
    }

    public void setPool_name(String pool_name) {
        this.pool_name = pool_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
