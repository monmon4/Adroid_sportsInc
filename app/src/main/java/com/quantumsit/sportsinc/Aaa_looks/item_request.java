package com.quantumsit.sportsinc.Aaa_looks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mona on 27-Dec-17.
 */

public class item_request implements Serializable {
    String creation_date;
    String course_name_and_class_name;
    String class_date;


    public item_request(String creation_date, String course_name_and_class_name, String class_date) {
        this.creation_date = creation_date;
        this.course_name_and_class_name  = course_name_and_class_name;
        this.class_date = class_date;
    }

}
