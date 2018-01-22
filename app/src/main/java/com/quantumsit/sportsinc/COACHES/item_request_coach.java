package com.quantumsit.sportsinc.COACHES;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mona on 28-Dec-17.
 */

public class item_request_coach {

    String creation_date, request_for, course_name , class_name, date, accepted;

    int status;

    public item_request_coach(String creation_date, String request_for, String course_name_and_class_number, String date, String accepted) {
        this.creation_date = creation_date;
        this.request_for = request_for;
        this.course_name = course_name_and_class_number;
        this.date = date;
        this.accepted = accepted;
    }

    public item_request_coach(JSONObject object) {
        try {
            this.request_for = "request for: "+ object.getString("title");
            String dateFormated =  object.getString("date_request");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.date = formatter.format(date);


            dateFormated =  object.getString("c_date");
            formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.creation_date = formatter.format(date);

            status = object.getInt("status");

            this.course_name = object.getString("course_name");;
            this.class_name = object.getString("sub_course_name");;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
