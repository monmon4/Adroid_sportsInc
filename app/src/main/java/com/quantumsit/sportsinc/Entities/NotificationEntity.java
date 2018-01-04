package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;

/**
 * Created by Bassam on 1/4/2018.
 */

public class NotificationEntity implements Serializable {
    String subject ,content ,date ,from ,type;

    int read;
    public NotificationEntity() {
    }

    public NotificationEntity(String subject, String content, String date, String from, String type,int read) {
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.from = from;
        this.type = type;
        this.read = read;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
