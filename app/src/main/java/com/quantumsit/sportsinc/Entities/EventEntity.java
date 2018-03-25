package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Bassam on 1/3/2018.
 */

public class EventEntity implements Serializable {
    String title,time,description, imgUrl , eventUrl ,eventFileUrl;
    Date date;
    int event_id;

    EventEntity(){}

    public EventEntity(String title, Date date, String time, String description) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public EventEntity(JSONObject object){
        try {
            this.event_id = object.getInt("id");
            this.imgUrl = object.getString("ImageUrl");
            this.eventUrl = object.getString("eventUrl");
            this.title = object.getString("Title");
            this.time = object.getString("time");
            time = time.substring(0,time.length()-3);
            String dateFormated =  object.getString("date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            date = formatter.parse(dateFormated);
            this.description = object.getString("description");
            this.eventFileUrl = object.getString("event_fileUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventFileUrl() {
        return eventFileUrl;
    }
}
