package com.quantumsit.sportsinc.Aaa_looks;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_request {
    String subject , creation_date;
    String course_name , class_name;
    String class_date;

    int status;

    public item_request(String creation_date, String course_name_and_class_name, String class_date) {
        this.creation_date = creation_date;
        this.course_name  = course_name_and_class_name;
        this.class_date = class_date;
    }
    public item_request(JSONObject object) {
        try {
            this.subject = object.getString("title");
            String dateFormated =  object.getString("date_request");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.class_date = formatter.format(date);


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
