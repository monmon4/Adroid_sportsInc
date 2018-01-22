package com.quantumsit.sportsinc.COACHES;

/**
 * Created by Bassam on 1/22/2018.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bassam on 1/22/2018.
 */

public class item_classSpinner_coach implements Serializable {
    String class_name , class_date;
    int class_id , course_id;

    public item_classSpinner_coach(JSONObject jsonObject) {
        try {
            class_id = jsonObject.getInt("class_id");
            class_name = "class" + jsonObject.getString("class_name");
            course_id = jsonObject.getInt("course_id");
            class_date = jsonObject.getString("class_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_date() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = df.parse(class_date);

            df = new SimpleDateFormat("dd/MM/yyyy");
            class_date = df.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}