package com.quantumsit.sportsinc.Entities;

import android.content.Context;
import android.content.res.Resources;

import com.quantumsit.sportsinc.Aaa_data.GlobalVars;
import com.quantumsit.sportsinc.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mona on 26-Dec-17.
 */

public class item_single_scores {

    String course_name, group_name, class_date, coach_name, coach_notes , personName;
    int score, class_number  ,person_id;
    int attend; // 0 for Absent , 1 for attend

    public item_single_scores(String course_name, String group_name, String class_date, String coach_name, String coach_notes, int attend ,int score, int class_number) {
        this.course_name = course_name;
        this.group_name = group_name;
        this.class_date = class_date;
        this.coach_name = coach_name;
        this.coach_notes = coach_notes;
        this.attend = attend;
        this.score = score;
        this.class_number = class_number;
    }

    public item_single_scores(JSONObject result){
        try {
            /*
            * get the info. of Trainee Class
            * his score in that class and if he is attended or not
            * if he attended that class it's value 1 else 0
            * */
            Context context = GlobalVars.getContext();
            this.course_name = result.getString(context.getString(R.string.select_course_name));
            this.group_name = result.getString(context.getString(R.string.select_group_name));
            this.class_date = result.getString(context.getString(R.string.select_class_date));
            this.class_number = result.getInt(context.getString(R.string.select_class_name));
            this.personName = result.getString(context.getString(R.string.select_person_name));
            this.person_id = result.getInt(context.getString(R.string.select_person_id));
            this.score = result.getInt(context.getString(R.string.select_score));
            this.attend = result.getInt(context.getString(R.string.select_attend));
            this.coach_name = result.getString(context.getString(R.string.select_name));
            this.coach_notes = result.getString(context.getString(R.string.select_coach_note));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getClass_date() {
        return class_date;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public String getCoach_notes() {
        return coach_notes;
    }

    public int getScore() {
        return score;
    }

    public int getClass_number() {
        return class_number;
    }

    public int getAttend() {
        return attend;
    }

    public String getPersonName() {
        return personName;
    }

    public int getPerson_id() {
        return person_id;
    }
}
