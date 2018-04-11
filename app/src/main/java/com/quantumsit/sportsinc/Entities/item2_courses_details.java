package com.quantumsit.sportsinc.Entities;

import android.support.v4.util.ArrayMap;

import java.io.Serializable;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item2_courses_details implements Serializable{

    String coach_name;
    String[] day, time;
    boolean available;


    public item2_courses_details(String coach_name, String[] day, String[]time, boolean available) {
        this.coach_name = coach_name;
        this.day = day;
        this.time = time;
        this.available = available;
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

    public boolean isAvailable() {
        return available;
    }
}
