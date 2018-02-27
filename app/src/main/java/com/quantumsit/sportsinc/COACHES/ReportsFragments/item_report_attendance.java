package com.quantumsit.sportsinc.COACHES.ReportsFragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mona_ on 12/28/2017.
 */

public class item_report_attendance {

    String date, course_name, class_number;
    int class_id , attend;
    Date class_date;

    public item_report_attendance(String date, int attend, String course_name, String class_number) {
        this.date = date;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            class_date = df.parse(date);
            df = new SimpleDateFormat("dd/MM/yyyy");
            this.date = df.format(class_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.attend = attend;
        this.course_name = course_name;
        this.class_number = class_number;
    }

    public item_report_attendance(JSONObject jsonObject) {
        try {
            class_id = jsonObject.getInt("class_id");
            class_number = "class " + jsonObject.getString("class_name");
            course_name = jsonObject.getString("course_name");
            attend = jsonObject.getInt("attend");
            date = jsonObject.getString("class_Date");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            class_date = df.parse(date);

            df = new SimpleDateFormat("dd/MM/yyyy");
            date = df.format(class_date);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
