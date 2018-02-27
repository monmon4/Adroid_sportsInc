package com.quantumsit.sportsinc.COACHES;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mona on 28-Dec-17.
 */

public class item_request_coach implements Serializable {

    String creation_date, request_for, course_name , class_name, date, accepted;

    Date requestDate;
    String personType;
    String person;
    String content;
    String course;
    String group;

    int request_ID ;
    int status;

    public item_request_coach(){

    }

    public item_request_coach(String creation_date, String request_for, String course_name_and_class_number, String date, String accepted) {
        this.creation_date = creation_date;
        this.request_for = request_for;
        this.course_name = course_name_and_class_number;
        this.date = date;
        this.accepted = accepted;
    }

    public item_request_coach(JSONObject object) {
        try {
            request_ID = object.getInt("id");
            this.request_for = "request for: " + object.getString("title");
            String dateFormated = object.getString("date_request");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.date = formatter.format(date);

            dateFormated = object.getString("c_date");
            formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.creation_date = formatter.format(date);

            status = object.getInt("status");

            this.course_name = object.getString("course_name");
            this.class_name = object.getString("sub_course_name");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void fillRequest(JSONObject object){
        try {
            Date date;
            DateFormat outdateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            DateFormat creationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.creation_date = object.getString("c_date");
            date = creationDateFormat.parse(creation_date);
            this.creation_date = outdateFormat.format(date);

            this.request_for = object.getString("title");
            String dateFormated =  object.getString("date_request");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            this.requestDate = formatter.parse(dateFormated);
            this.personType = object.getString("type");;
            this.person = object.getString("name");
            status = object.getInt("status");
            this.content = object.getString("content");;
            this.course = object.getString("course_name");;
            this.group = object.getString("sub_course_name");;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getRequest_ID() {
        return request_ID;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getRequest_for() {
        return request_for;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getDate() {
        return date;
    }

    public String getAccepted() {
        return accepted;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public String getPersonType() {
        return personType;
    }

    public String getPerson() {
        return person;
    }

    public String getContent() {
        return content;
    }

    public String getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public int getStatus() {
        return status;
    }
}
