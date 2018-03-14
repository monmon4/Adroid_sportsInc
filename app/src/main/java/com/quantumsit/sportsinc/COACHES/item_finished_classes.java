package com.quantumsit.sportsinc.COACHES;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mona on 28-Dec-17.
 */

public class item_finished_classes implements Serializable{

    String class_name;
    String class_date , class_note;
    int class_id;

    public item_finished_classes(String class_name, String class_date) {
        this.class_name = class_name;
        this.class_date = class_date;
    }

    public item_finished_classes(JSONObject jsonObject) {
        try {
            class_id = jsonObject.getInt("class_id");
            class_name = "class " + jsonObject.getString("class_name");
            class_date = jsonObject.getString("class_Date");
            class_note = jsonObject.getString("class_note");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date myDate = df.parse(class_date);
            df = new SimpleDateFormat("dd/MM/yyyy");
            class_date = df.format(myDate);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }
}
