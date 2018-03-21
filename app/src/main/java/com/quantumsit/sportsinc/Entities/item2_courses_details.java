package com.quantumsit.sportsinc.Entities;

import android.support.v4.util.ArrayMap;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item2_courses_details {

    String coach_name;
    ArrayMap day_time;


    public item2_courses_details(String coach_name, ArrayMap day_time) {
        this.coach_name = coach_name;
        this.day_time = day_time;
    }

    public String getCoach_name() {
        return coach_name;
    }

    public ArrayMap getDay_time() {
        return day_time;
    }


}
