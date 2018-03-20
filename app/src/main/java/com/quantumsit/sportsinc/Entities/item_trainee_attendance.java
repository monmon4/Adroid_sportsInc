package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mona on 31-Dec-17.
 */

public class item_trainee_attendance {

    String name;
    int trainee_id;
    boolean attended;
    String attendance_notes;

    public item_trainee_attendance(String name, boolean attended, String attendance_notes) {
        this.name = name;
        this.attended = attended;
        this.attendance_notes = attendance_notes;
    }

    public item_trainee_attendance(JSONObject jsonObject) {
        try {
            attendance_notes = jsonObject.getString("coach_note");
            name = jsonObject.getString("trainee_name");
            trainee_id = jsonObject.getInt("trainee_id");
            int attend = jsonObject.getInt("trainee_attend");
            if (attend == 1)
                attended = true;
            else
                attended = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getTrainee_id() {
        return trainee_id;
    }

    public boolean isAttended() {
        return attended;
    }

    public String getAttendance_notes() {
        return attendance_notes;
    }
}
