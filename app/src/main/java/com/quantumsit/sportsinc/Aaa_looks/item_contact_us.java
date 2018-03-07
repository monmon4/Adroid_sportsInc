package com.quantumsit.sportsinc.Aaa_looks;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_contact_us {
    String date, start_time , end_time;

    public item_contact_us(String date, String start_time, String end_time) {
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public item_contact_us(JSONObject jsonObject) {
        try {
            this.date = jsonObject.getString("day");
            this.start_time = jsonObject.getString("day_start");
            this.end_time = jsonObject.getString("day_end");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
