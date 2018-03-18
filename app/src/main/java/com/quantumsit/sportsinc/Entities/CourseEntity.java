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
    String ImageUrl;
    String CourseName;
    Date StartDate;
    Date EndDate;
    String price;
    String level;
    String classes_Num;
    String description;
    String classDur;

    public CourseEntity() {
    }

    public CourseEntity(String courseName, Date startDate, Date endDate, String price, String level, String classes_Num, String description) {
        ImageUrl = "";
        CourseName = courseName;
        StartDate = startDate;
        EndDate = endDate;
        this.price = price;
        this.level = level;
        this.classes_Num = classes_Num;
        this.description = description;
    }

    public CourseEntity(JSONObject object){
        try {
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

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClasses_Num() {
        return classes_Num;
    }

    public void setClasses_Num(String classes_Num) {
        this.classes_Num = classes_Num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassDur() {
        return classDur;
    }

    public void setClassDur(String classDur) {
        this.classDur = classDur;
    }
}
