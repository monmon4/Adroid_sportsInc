package com.quantumsit.sportsinc.COACHES.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mona_ on 12/28/2017.
 */

public class item_report_attendance implements Serializable {

    String date, course_name, group_name , class_number;
    int course_id ,group_id , class_id , attend;
    Date class_date;


    public item_report_attendance(String date, int attend, String course_name, String class_number) {
        this.date = date;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            class_date = df.parse(date);
            df = new SimpleDateFormat("dd/MM/yyyy");
            this.date = df.format(class_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.attend = attend;
        this.course_name = course_name;
        this.class_number = class_number;
    }

    public item_report_attendance(JSONObject jsonObject) {
        try {
            class_id = jsonObject.getInt("class_id");
            class_number = "session " + jsonObject.getString("class_name");
            course_name = jsonObject.getString("course_name");
            attend = jsonObject.getInt("attend");
            date = jsonObject.getString("class_Date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            class_date = df.parse(date);

            df = new SimpleDateFormat("dd/MM/yyyy");
            date = df.format(class_date);

            course_id = jsonObject.getInt("course_id");
            group_id = jsonObject.getInt("group_id");
            group_name = jsonObject.getString("group_name");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getClass_number() {
        return class_number;
    }

    public int getCourse_id() {
        return course_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public int getAttend() {
        return attend;
    }

    public Date getClass_date() {
        return class_date;
    }
}
