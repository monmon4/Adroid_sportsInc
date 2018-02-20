package com.quantumsit.sportsinc.Aaa_data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bassam on 1/24/2018.
 */

public class StartClass_info implements Serializable {



    private int id, class_id, attendance_id, coach_attendance;
    private String coach_attendance_notes;
    private int coach_id, check_rule_id, check_rule;
    private String check_rule_notes;

    public StartClass_info(){

    }

    public StartClass_info(int id, int class_id, int attendance_id, int coach_attendance, String coach_attendance_notes, int coach_id, int check_rule_id, int check_rule, String check_rule_notes) {
        this.id = id;
        this.class_id = class_id;
        this.attendance_id = attendance_id;
        this.coach_attendance = coach_attendance;
        this.coach_attendance_notes = coach_attendance_notes;
        this.coach_id = coach_id;
        this.check_rule_id = check_rule_id;
        this.check_rule = check_rule;
        this.check_rule_notes = check_rule_notes;
    }

    public void StartClass_info_attendance(JSONObject response) {
        try {
            class_id = response.getInt("class_id");
            coach_id = response.getInt("user_id");
            attendance_id = response.getInt("id");
            coach_attendance = response.getInt("attend");
            coach_attendance_notes = response.getString("notes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void StartClass_info_rules(JSONObject response) {
        try {
            class_id = response.getInt("class_id");
            check_rule_id = response.getInt("id");
            check_rule = response.getInt("rule_id");
            check_rule_notes = response.getString("note");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(int attendance_id) {
        this.attendance_id = attendance_id;
    }

    public int getCoach_attendance() {
        return coach_attendance;
    }

    public void setCoach_attendance(boolean coach_attendancee) {
        if(coach_attendancee)
            this.coach_attendance = 1;
        else
            this.coach_attendance = 0;
    }

    public String getCoach_attendance_notes() {
        return coach_attendance_notes;
    }

    public void setCoach_attendance_notes(String coach_attendance_notes) {
        this.coach_attendance_notes = coach_attendance_notes;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }

    public int getCheck_rule_id() {
        return check_rule_id;
    }

    public void setCheck_rule_id(int check_rule_id) {
        this.check_rule_id = check_rule_id;
    }

    public int getCheck_rule() {
        return check_rule;
    }

    public void setCheck_rule(boolean check_rulee) {
        if (check_rulee)
            this.check_rule = 1;
        else
            this.check_rule = 0;
    }

    public String getCheck_rule_notes() {
        return check_rule_notes;
    }

    public void setCheck_rule_notes(String check_rule_notes) {
        this.check_rule_notes = check_rule_notes;
    }
}
