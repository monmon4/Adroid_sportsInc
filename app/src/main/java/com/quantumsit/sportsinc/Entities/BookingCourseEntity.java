package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bassam on 12/26/2017.
 */

public class BookingCourseEntity implements Serializable {

    String trainee_name;
    String trainee_id;
    CourseEntity courseEntity;
    String class_name;
    int class_id;

    public BookingCourseEntity() {
    }

    public BookingCourseEntity(String trainee_name,String trainee_id, CourseEntity courseEntity, String class_name, int class_id) {
        this.trainee_name = trainee_name;
        this.courseEntity = courseEntity;
        this.class_name = class_name;
        this.class_id = class_id;
        this.trainee_id = trainee_id;
    }

    public String getTrainee_name() {
        return trainee_name;
    }

    public void setTrainee_name(String trainee_name) {
        this.trainee_name = trainee_name;
    }

    public CourseEntity getCourseEntity() {
        return courseEntity;
    }

    public void setCourseEntity(CourseEntity courseEntity) {
        this.courseEntity = courseEntity;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getTrainee_id() {
        return trainee_id;
    }
}

