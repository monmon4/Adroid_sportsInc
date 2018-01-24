package com.quantumsit.sportsinc.Aaa_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 1/24/2018.
 */

public class MyClass_info {
    private int class_id;
    private String class_name;
    private String class_date;
    private String class_note;
    private int group_id;

    public MyClass_info(int class_id, String class_name, String class_date, String class_note, int group_id) {
        this.class_id = class_id;
        this.class_name = class_name;
        this.class_date = class_date;
        this.class_note = class_note;
        this.group_id = group_id;
    }

    public MyClass_info(JSONObject response) {
        try {
            class_id = response.getInt("class_id");
            class_name = "class " + response.getString("class_number");
            class_date = response.getString("class_date");
            class_note = response.getString("class_notes");
            group_id = response.getInt("group_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public String getClass_note() {
        return class_note;
    }

    public void setClass_note(String class_note) {
        this.class_note = class_note;
    }

}
