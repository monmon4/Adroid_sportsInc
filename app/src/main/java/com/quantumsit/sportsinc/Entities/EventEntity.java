package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;

/**
 * Created by Bassam on 1/3/2018.
 */

public class EventEntity implements Serializable {
    String title,date,time,description;

    EventEntity(){}

    public EventEntity(String title, String date, String time, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}