package com.quantumsit.sportsinc.Entities;

import android.support.v4.util.ArrayMap;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item2_courses_details {

    String coach_name;
    String[] day, time;


    public item2_courses_details(String coach_name, String[] day, String[]time) {
        this.coach_name = coach_name;
        this.day = day;
        this.time = time;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public String[] getDay() {
        return day;
    }

    public String[] getTime() {
        return time;
    }
}
