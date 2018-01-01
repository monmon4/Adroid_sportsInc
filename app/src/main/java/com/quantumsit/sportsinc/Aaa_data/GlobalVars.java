package com.quantumsit.sportsinc.Aaa_data;

import android.app.Application;

/**
 * Created by Mona on 31-Dec-17.
 */

public class GlobalVars extends Application {

    int user_is ; //1 parent, 2 coach, 3 admin

    boolean coach_currentclass_rules, coach_currentclass_attendance,
            coach_currentclass_scores, coach_currentclass_notes;

    public int getUser_is() {
        return user_is;
    }

    public void setUser_is(int user_is) {
        this.user_is = user_is;
    }

    public boolean isCoach_currentclass_rules() {
        return coach_currentclass_rules;
    }

    public void setCoach_currentclass_rules(boolean coach_currentclass_rules) {
        this.coach_currentclass_rules = coach_currentclass_rules;
    }

    public boolean isCoach_currentclass_attendance() {
        return coach_currentclass_attendance;
    }

    public void setCoach_currentclass_attendance(boolean coach_currentclass_attendance) {
        this.coach_currentclass_attendance = coach_currentclass_attendance;
    }

    public boolean isCoach_currentclass_scores() {
        return coach_currentclass_scores;
    }

    public void setCoach_currentclass_scores(boolean coach_currentclass_scores) {
        this.coach_currentclass_scores = coach_currentclass_scores;
    }

    public boolean isCoach_currentclass_notes() {
        return coach_currentclass_notes;
    }

    public void setCoach_currentclass_notes(boolean coach_currentclass_notes) {
        this.coach_currentclass_notes = coach_currentclass_notes;
    }
}
