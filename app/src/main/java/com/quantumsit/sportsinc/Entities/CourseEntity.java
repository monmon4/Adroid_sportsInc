package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bassam on 12/26/2017.
 */

public class CourseEntity implements Serializable {
    int course_id;
    String ImageUrl;
    String CourseName;
    Date StartDate;
    Date EndDate;
    String price;
    String level;
    String classes_Num;
    String description;
    String classDur;

    public CourseEntity(JSONObject object){
        try {
            course_id = object.getInt("id");
            CourseName = object.getString("name");
            ImageUrl = object.getString("ImageUrl");
            String dateFormated =  object.getString("start_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            StartDate = formatter.parse(dateFormated);
            dateFormated =  object.getString("end_date");
            EndDate = formatter.parse(dateFormated);
            this.price = object.getString("price");
            this.level = object.getString("level");
            this.classes_Num = object.getString("no_of_classes");;
            this.description = object.getString("description");;
            this.classDur = object.getString("class_duration");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public String getCourseName() {
        return CourseName;
    }


    public String getImageUrl() {
        return ImageUrl;
    }


    public Date getStartDate() {
        return StartDate;
    }


    public Date getEndDate() {
        return EndDate;
    }


    public String getPrice() {
        return price;
    }


    public String getLevel() {
        return level;
    }


    public String getClasses_Num() {
        return classes_Num;
    }

    public String getDescription() {
        return description;
    }

    public String getClassDur() {
        return classDur;
    }


    public int getCourse_id() {
        return course_id;
    }

}
