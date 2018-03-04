package com.quantumsit.sportsinc.Home_fragments;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Mona on 04-Mar-18.
 */

public class item_courses {

    String price, img;

    String name, level, no_of_classes, description;

    public item_courses(String price, String img, String name, String level, String no_of_classes, String description) {
        this.price = price;
        this.img = img;
        this.name = name;
        this.level = level;
        this.no_of_classes = no_of_classes;
        this.description = description;
    }

    public item_courses(JSONObject object){
        try {
            this.name = object.getString("name");
            this.img = object.getString("ImageUrl");
            this.price = object.getString("price");
            this.level = object.getString("level");
            this.no_of_classes = object.getString("no_of_classes");
            this.description = object.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
