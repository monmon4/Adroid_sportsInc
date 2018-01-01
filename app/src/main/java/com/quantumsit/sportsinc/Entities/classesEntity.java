package com.quantumsit.sportsinc.Entities;

import java.io.Serializable;

/**
 * Created by Bassam on 12/24/2017.
 */

public class classesEntity implements Serializable {
    String courseName, groupName, className, status, coachName, adminName, poolName;
    String startTime, endTime, reason, postpondTime;

    public classesEntity() {
    }

    public classesEntity(String courseName, String groupName, String className, String status, String coachName, String adminName, String poolName, String startTime, String endTime) {
        this.courseName = courseName;
        this.groupName = groupName;
        this.className = className;
        this.status = status;
        this.coachName = coachName;
        this.adminName = adminName;
        this.poolName = poolName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public classesEntity(String courseName, String groupName, String className, String status, String coachName, String adminName, String poolName, String startTime, String endTime, String reason, String postpondTime) {
        this.courseName = courseName;
        this.groupName = groupName;
        this.className = className;
        this.status = status;
        this.coachName = coachName;
        this.adminName = adminName;
        this.poolName = poolName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.postpondTime = postpondTime;
    }

    public classesEntity(String courseName, String groupName, String className, String status, String coachName, String adminName, String poolName, String startTime, String endTime, String reason) {
        this.courseName = courseName;
        this.groupName = groupName;
        this.className = className;
        this.status = status;
        this.coachName = coachName;
        this.adminName = adminName;
        this.poolName = poolName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
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
}
