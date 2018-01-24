package com.quantumsit.sportsinc.ADMINS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Mona on 04-Jan-18.
 */

public class item_current_classes implements Serializable{

    String class_number, class_date, startTime ,endTime ,coach_Name;
    int id,coach_id;

    public item_current_classes(String class_number, String class_date, String startTime ) {
        this.class_number = class_number;
        this.class_date = class_date;
        this.startTime = startTime;
    }

    public item_current_classes(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("class_ID");
            coach_id = jsonObject.getInt("coach_id");
            class_number = " Class "+jsonObject.getString("class_Name");
            coach_Name = jsonObject.getString("coach_name");
            String CDate = jsonObject.getString("class_Date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = df.parse(CDate);
            df = new SimpleDateFormat("MMM dd");
            class_date = df.format(theDate);

            startTime = jsonObject.getString("class_start_time");
            this.endTime = jsonObject.getString("class_end_time");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public String getClass_date() {
        return class_date;
    }

    public void setClass_date(String class_date) {
        this.class_date = class_date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoach_Name() {
        return coach_Name;
    }

    public void setCoach_Name(String coach_Name) {
        this.coach_Name = coach_Name;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }
}
