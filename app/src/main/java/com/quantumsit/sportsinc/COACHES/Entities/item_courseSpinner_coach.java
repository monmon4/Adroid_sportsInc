package com.quantumsit.sportsinc.COACHES.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bassam on 1/22/2018.
 */

public class item_courseSpinner_coach implements Serializable{

    int course_id;
    String course_name;

    public item_courseSpinner_coach(JSONObject jsonObject) {
        try {
            course_id = jsonObject.getInt("course_id");
            course_name = jsonObject.getString("course_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof item_courseSpinner_coach){
            item_courseSpinner_coach item = (item_courseSpinner_coach) obj;
            if (this.course_id == item.course_id)
                return true;
        }
        return false;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}
