package com.quantumsit.sportsinc.Entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bassam on 12/24/2017.
 */

public class classesEntity implements Serializable {
    String courseName, groupName, className, status, coachName, adminName, poolName;
    String startTime, endTime, reason, postpondTime,postpondDate ,classdate;
    int class_id ,Group_id ,Course_id ,Admin_id ,Coach_id;

    public classesEntity() {
    }

    public classesEntity(JSONObject jsonObject) {
        try {
            this.class_id = jsonObject.getInt("class_id");
            this.className = "Class "+jsonObject.getInt("class_number");
            String dateFormated = jsonObject.getString("class_date");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(dateFormated);
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            this.classdate = formatter.format(date);
            this.courseName = jsonObject.getString("Courses_name");
            this.groupName = jsonObject.getString("Groups_Name");
            int state = jsonObject.getInt("status");
            switch (state) {
                case 0:
                    this.status = "Running";
                    break;
                case 1:
                    this.status = "Canceled";
                    break;
                case 2:
                    this.status = "Postponed";
                    break;
                case 3:
                    this.status = "UpComing";
                    break;
                case 4:
                    this.status = "Finished";
                    break;

            }
            this.Coach_id = jsonObject.getInt("coach_id");
            this.coachName = jsonObject.getString("coach_name");
            this.Admin_id = jsonObject.getInt("admin_id");
            this.adminName = jsonObject.getString("admin_name");
            this.poolName = jsonObject.getString("Pool_Name");
            this.startTime = jsonObject.getString("class_time");
            this.endTime= jsonObject.getString("class_end_time");
            this.reason = jsonObject.getString("class_notes");
            this.postpondDate = jsonObject.getString("postpone_date");
            this.postpondTime = jsonObject.getString("postpone_time");
            postpondTime = postpondTime.substring(0,postpondTime.length()-3);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof classesEntity){
            classesEntity entity = (classesEntity) obj;
            if (this.class_id == entity.class_id)
                return true;
            return false;
        }
        return true;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPostpondDate() {
        return postpondDate;
    }

    public void setPostpondDate(String postpondDate) {
        this.postpondDate = postpondDate;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(int group_id) {
        Group_id = group_id;
    }

    public int getCourse_id() {
        return Course_id;
    }

    public void setCourse_id(int course_id) {
        Course_id = course_id;
    }

    public int getAdmin_id() {
        return Admin_id;
    }

    public void setAdmin_id(int admin_id) {
        Admin_id = admin_id;
    }

    public int getCoach_id() {
        return Coach_id;
    }

    public void setCoach_id(int coach_id) {
        Coach_id = coach_id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPostpondTime() {
        return postpondTime;
    }

    public void setPostpondTime(String postpondTime) {
        this.postpondTime = postpondTime;
    }

    public String getClassdate() {
        return classdate;
    }

    public void setClassdate(String classdate) {
        this.classdate = classdate;
    }
}
