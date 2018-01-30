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

    String class_number, class_date, startTime ,endTime , postpone_date, postpone_startTime ,postpone_endTime ,coach_Name , poolName;
    int id,coach_id , status;

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
            poolName = jsonObject.getString("pool_name");
            status = jsonObject.getInt("class_status");

            String CDate = jsonObject.getString("class_Date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date theDate = df.parse(CDate);
            df = new SimpleDateFormat("MMM dd");
            class_date = df.format(theDate);
            startTime = jsonObject.getString("class_start_time");
            startTime = startTime.substring(0,startTime.length()-3);
            endTime = jsonObject.getString("class_end_time");
            endTime = endTime.substring(0,endTime.length()-3);

            CDate = jsonObject.getString("class_postpone_Date");
            df = new SimpleDateFormat("yyyy-MM-dd");
            theDate = df.parse(CDate);
            df = new SimpleDateFormat("MMM dd");
            postpone_date = df.format(theDate);
            postpone_startTime = jsonObject.getString("postpone_start_time");
            postpone_startTime = postpone_startTime.substring(0,postpone_startTime.length()-3);
            postpone_endTime = jsonObject.getString("postpone_end_time");
            postpone_endTime = postpone_endTime.substring(0,postpone_endTime.length()-3);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
