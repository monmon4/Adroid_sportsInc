package com.quantumsit.sportsinc.Aaa_data;

/**
 * Created by Bassam on 1/24/2018.
 */

public class Rule_info {
    private int rule_id;
    private int class_id;
    private String rule_name;
    private int rule_check;
    private String rule_note;

    public Rule_info(int rule_id, int class_id, String rule_name, int rule_check, String rule_note) {
        this.rule_id = rule_id;
        this.class_id = class_id;
        this.rule_name = rule_name;
        this.rule_check = rule_check;
        this.rule_note = rule_note;
    }

    public String getRule_name() {
        return rule_name;
    }

    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
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

    public void setRule_check(int rule_check) {
        this.rule_check = rule_check;
    }
}
