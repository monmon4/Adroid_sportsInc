package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;

/**
 * Created by Bassam on 12/26/2017.
 */

public class BookingCourseEntityFragment {

    String trainee_name;
    int trainee_id;
    String course_name;
    int course_id;
    String course_image;
    String price;
    String class_name;
    int class_id;

    public BookingCourseEntityFragment() {
    }

    public BookingCourseEntityFragment(String trainee_name, int trainee_id,
                                       String course_name, int course_id,
                                       String course_image, String price,
                                       String class_name, int class_id) {
        this.trainee_name = trainee_name;
        this.trainee_id = trainee_id;
        this.course_name = course_name;
        this.course_id = course_id;
        this.course_image = course_image;
        this.price = price;
        this.class_name = class_name;
        this.class_id = class_id;
    }

    public String getTrainee_name() {
        return trainee_name;
    }

    public int getTrainee_id() {
        return trainee_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_image() {
        return course_image;
    }

    public String getPrice() {
        return price;
    }

    public String getClass_name() {
        return class_name;
    }

    public int getClass_id() {
        return class_id;
    }
}

