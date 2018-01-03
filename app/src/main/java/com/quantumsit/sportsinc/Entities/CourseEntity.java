package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bassam on 12/26/2017.
 */

public class CourseEntity implements Serializable {
    String CourseName ,StartDate ,EndDate ,price ,level ,classes_Num ,description;

    public CourseEntity() {
    }


    public CourseEntity(String courseName, String startDate, String endDate, String price, String level, String classes_Num, String description) {
        CourseName = courseName;
        StartDate = startDate;
        EndDate = endDate;
        this.price = price;
        this.level = level;
        this.classes_Num = classes_Num;
        this.description = description;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
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
}
