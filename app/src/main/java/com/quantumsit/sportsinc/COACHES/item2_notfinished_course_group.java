package com.quantumsit.sportsinc.COACHES;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassam on 1/23/2018.
 */

public class item2_notfinished_course_group {
    String courseName , groupName ,poolName;
    int course_id , group_id , admin_id , coach_id;

    public item2_notfinished_course_group(String courseName, String groupName, String poolName, int course_id, int group_id, int admin_id, int coach_id) {
        this.courseName = courseName;
        this.groupName = groupName;
        this.poolName = poolName;
        this.course_id = course_id;
        this.group_id = group_id;
        this.admin_id = admin_id;
        this.coach_id = coach_id;
    }

    public item2_notfinished_course_group(JSONObject jsonObject) {
        try {
            course_id = jsonObject.getInt("course_id");
            courseName = jsonObject.getString("course_name");
            group_id = jsonObject.getInt("group_id");
            groupName = jsonObject.getString("group_name");
            poolName = jsonObject.getString("pool_name");
            admin_id = jsonObject.getInt("admin_id");
            coach_id = jsonObject.getInt("coach_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof item2_notfinished_course_group){
            item2_notfinished_course_group item = (item2_notfinished_course_group) obj;
            if (this.group_id == item.group_id)
                return true;
        }
        return false;
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

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(int coach_id) {
        this.coach_id = coach_id;
    }
}
