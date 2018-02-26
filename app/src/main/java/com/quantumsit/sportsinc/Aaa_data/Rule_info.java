package com.quantumsit.sportsinc.Aaa_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 1/24/2018.
 */

public class Rule_info {
    private int rule_id;
    private int class_id;
    private int rule_check;
    private String rule_note="";
    private int user_id;

    public Rule_info(){}

    public Rule_info(int rule_id, int class_id, int rule_check, String rule_note,int user_id) {
        this.rule_id = rule_id;
        this.class_id = class_id;
        this.user_id = user_id;
        this.rule_check = rule_check;
        this.rule_note = rule_note;
    }

    public Rule_info(JSONObject jsonObject , int class_id) {
        try {
            this.rule_id = jsonObject.getInt("id");
            this.class_id = class_id;
            this.rule_check = jsonObject.getInt("rule_checked");
            this.user_id = jsonObject.getInt("user_id");
            this.rule_note = jsonObject.getString("note");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRule_note() {
        return rule_note;
    }

    public void setRule_note(String rule_note) {
        this.rule_note = rule_note;
    }

    public int getRule_id() {
        return rule_id;
    }

    public void setRule_id(int rule_id) {
        this.rule_id = rule_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getRule_check() {
        return rule_check;
    }

    public boolean getSelected(){
        if (rule_check == 1)
            return true;
        else
            return false;
    }

    public void setSelected(boolean status){
        if (status)
            setRule_check(1);
        else
            setRule_check(0);
    }
    public void setRule_check(int rule_check) {
        this.rule_check = rule_check;
    }
}
